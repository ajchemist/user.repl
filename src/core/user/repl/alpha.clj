(ns user.repl.alpha
  (:require
   [clojure.java.io :as jio]
   [clojure.stacktrace :as stacktrace]
   [user.clojure.core.patch.alpha :as core.patch]
   [user.repl.alpha.reader :as reader]
   [clojure.tools.namespace.find :as t.n.f]
   [ns-tracker.core :as nt]
   ))


(defmacro r
  [sym]
  `(try
     (require (quote ~(symbol (namespace sym))))
     (resolve (quote ~sym))
     (catch Throwable _#
       (println "Failed to resolve:" (quote ~sym)))))


;;


(defn install-javadoc
  [{:keys [install-javadoc?]
    :or   {install-javadoc? true}
    :as   config-map}]
  (when (boolean install-javadoc?)
    ((r user.repl.alpha.javadoc/install-remote-javadoc)
     (:remote-javadoc (reader/read-config-maps (reader/default-configs "javadoc.edn")))))
  config-map)


(defn install-stacktrace
  [{:keys [pretty-stacktrace?]
    :or   {pretty-stacktrace? true}
    :as   config-map}]
  (when (boolean pretty-stacktrace?)
    ((r io.aviso.repl/install-pretty-exceptions)))
  config-map)


(defn install-meta
  [{:keys [install-meta?]
    :or   {install-meta? true}
    :as   config-map}]
  (when (boolean install-meta?)
    (core.patch/resolve-ns 'user.repl.alpha.meta))
  config-map)


;; * Live coding


(defn live-update-fns
  [paths meta-key]
  (transduce
    (comp
      (filter (fn [sym] (try (the-ns sym) (catch Throwable _ false))))
      (map the-ns)
      (map ns-publics)
      cat
      (filter (fn [[_ v]] (get (meta v) meta-key)))
      (map val)
      (map (juxt identity var-get))
      (filter (fn [[_ o]] (fn? o))))
    conj
    []
    (into #{} (t.n.f/find-namespaces (map jio/as-file paths)))))


(defn live-update-runner
  ([paths]
   (live-update-runner paths :repl/live-update))
  ([paths meta-key]
   (fn [event]
     (doseq [[v f] (live-update-fns paths meta-key)]
       (try
         (tap> [::run v])
         (f event)
         (catch Exception e
           (stacktrace/print-stack-trace e 6)))))))


(defn modified-namespaces-reloader
  [paths]
  (let [find-modified-namespaces (nt/ns-tracker paths)]
    (fn [_event]
      (let [modified-nses (find-modified-namespaces)]
        (tap> [::modified-namespaces modified-nses])
        (doseq [ns-sym modified-nses]
          (try
            (require ns-sym :reload)
            (catch Exception e
              (stacktrace/print-stack-trace e 20))))))))
