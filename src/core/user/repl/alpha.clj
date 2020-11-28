(ns user.repl.alpha
  (:require
   [user.clojure.core.patch.alpha :as core.patch]
   [user.repl.alpha.reader :as reader]
   ))


(defmacro r
  [sym]
  `(try
     (require (quote ~(symbol (namespace sym))))
     (resolve (quote ~sym))
     (catch Throwable _#
       (println "Failed to resolve:" (quote ~sym)))))


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
