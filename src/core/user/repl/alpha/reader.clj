(ns user.repl.alpha.reader
  (:require
   [clojure.edn :as edn]
   [clojure.walk :as walk]
   [clojure.java.io :as jio]
   )
  (:import
   java.io.File
   java.io.Reader
   java.io.PushbackReader
   ))


(def ^:dynamic *the-dir*
  "Thread-local directory context for resolving relative directories.
  Defaults to current directory. Should always hold an absolute directory
  java.io.File, never null."
  (jio/file (System/getProperty "user.dir")))


(defn read-edn
  "Read the edn file from the specified `reader`.
  This file should contain a single edn value. Empty files return nil.
  The reader will be read to EOF and closed."
  [^Reader reader]
  (let [EOF (Object.)]
    (with-open [rdr (PushbackReader. reader)]
      (let [val (edn/read {:eof EOF} rdr)]
        (if (identical? EOF val)
          nil
          (if (not (identical? EOF (edn/read {:eof EOF} rdr)))
            (throw (ex-info "Invalid file, expected edn to contain a single value." {}))
            val))))))


(defn slurp-edn
  "slurp and return edn-map"
  [f]
  (read-edn (jio/reader f)))


(defn slurp-config-map
  "Read `f` specified by the path-segments, slurp it, and read it as edn."
  [f]
  (let [val (try
              (slurp-edn f)
              (catch RuntimeException _t
                (throw (ex-info "Error reading edn." {:target f}))))]
    (if (map? val)
      val
      (throw (ex-info "Expected edn map." {:target f})))))


(defn- canonicalize-sym
  [s]
  (if (and (symbol? s) (nil? (namespace s)))
    (as-> (name s) n (symbol n n))
    s))


(defn- map-keys
  [m f]
  (reduce-kv
    (fn [acc k v] (assoc acc (f k) v))
    {} m))


(defn- canonicalize-all-syms
  [m]
  (walk/postwalk
    #(cond-> %
       (map? %) (map-keys canonicalize-sym)
       (vector? %) ((fn [v] (mapv canonicalize-sym v))))
    m))


(defn slurp-canonical-config-map
  [f]
  (-> f slurp-config-map))


(defn- merge-or-replace
  "If maps, merge, otherwise replace"
  [& vals]
  (when (some identity vals)
    (reduce (fn [ret val]
              (if (and (map? ret) (map? val))
                (merge ret val)
                (or val ret)))
            nil vals)))


(defn merge-config-maps
  "Merge multiple deps maps from left to right into a single deps map."
  [config-maps]
  (apply merge-with merge-or-replace config-maps))


(defn read-config-maps
  "Read the built-in clojure/tools/deps/deps.edn resource, and a set of deps-files,
  and merge them left to right into a single deps map."
  [config-files]
  (let [config-maps (map slurp-config-map config-files)]
    (merge-config-maps config-maps)))


(defn user-location
  "Use the same logic as clj to return the expected location of the user
  config path. Note that it's possible no file may exist at this location."
  [filename]
  (let [config-env (System/getenv "CLJ_CONFIG")
        xdg-env    (System/getenv "XDG_CONFIG_HOME")
        home       (System/getProperty "user.home")
        config-dir (cond
                     config-env config-env
                     xdg-env    (str xdg-env File/separator "clojure")
                     :else      (str home File/separator ".clojure"))]
    (str config-dir File/separator filename)))


(defn user-repl-location
  ""
  []
  (user-location "repl.edn"))


(defn default-configs
  [filename]
  (filterv
    #(-> % jio/file .exists)
    [(user-location filename) (str *the-dir* File/separator filename)]))
