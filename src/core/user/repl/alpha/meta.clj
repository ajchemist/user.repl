(ns user.repl.alpha.meta
  (:require
   [user.clojure.core.patch.alpha :refer [resolve-ns]]
   ))


(def r resolve)


(future
  (when (resolve-ns 'clojure.spec.alpha)
    (alias 'spec 'clojure.spec.alpha)
    (alter-meta! (r 'spec/fdef) assoc :style/indent [:defn]))


  (when (resolve-ns 'clojure.core.async)
    (alias 'async 'clojure.core.async)
    (alter-meta! (r 'async/put!) assoc :style/indent [:defn])
    (alter-meta! (r 'async/take!) assoc :style/indent [:defn])
    (alter-meta! (r 'async/pipeline) assoc :style/indent [:defn])
    (alter-meta! (r 'async/pipeline-async) assoc :style/indent [:defn])
    (alter-meta! (r 'async/pipeline-blocking) assoc :style/indent [:defn])
    )


  (when (resolve-ns 'clojure.java.jdbc)
    (alias 'jdbc 'clojure.java.jdbc)
    (alter-meta! (r 'jdbc/query) assoc :style/indent [:defn])
    (alter-meta! (r 'jdbc/execute!) assoc :style/indent [:defn])
    (alter-meta! (r 'jdbc/insert!) assoc :style/indent [:defn])
    (alter-meta! (r 'jdbc/insert-multi!) assoc :style/indent [:defn])
    )


  (when (resolve-ns 'clojure.data.xml)
    (alias 'data.xml 'clojure.data.xml)
    (alter-meta! (r 'data.xml/element) assoc :style/indent [:defn]))


  (when (resolve-ns 'datomic.api)
    (alter-meta! (r 'datomic.api/pull) assoc :style/indent [:defn])
    (alter-meta! (r 'datomic.api/pull-many) assoc :style/indent [:defn])
    (alter-meta! (r 'datomic.api/transact)  assoc :style/indent [:defn])
    (alter-meta! (r 'datomic.api/transact-async) assoc :style/indent [:defn])
    (alter-meta! (r 'datomic.api/filter) assoc :style/indent [:defn])
    )


  (when (resolve-ns 'datascript.core)
    (alter-meta! (r 'datascript.core/reset-conn!) assoc :style/indent [:defn])
    (alter-meta! (r 'datascript.core/pull)      assoc :style/indent [:defn])
    (alter-meta! (r 'datascript.core/pull-many) assoc :style/indent [:defn])
    (alter-meta! (r 'datascript.core/listen!)   assoc :style/indent [:defn])
    (alter-meta! (r 'datascript.core/transact)  assoc :style/indent [:defn])
    (alter-meta! (r 'datascript.core/transact!) assoc :style/indent [:defn])
    (alter-meta! (r 'datascript.core/db-with)   assoc :style/indent [:defn])
    )


  (when (resolve-ns 'com.stuartsierra.component)
    (alias 'component 'com.stuartsierra.component)
    (alter-meta! (r 'component/using) assoc :style/indent [:defn])
    (alter-meta! (r 'component/system-using) assoc :style/indent [:defn])
    (alter-meta! (r 'component/system-map) assoc :style/indent [:defn])
    (alter-meta! (r 'component/map->SystemMap) assoc :style/indent [:defn])
    (alter-meta! (r 'component/dependency-graph) assoc :style/indent [:defn])
    )


  (when (resolve-ns 'compojure.core)
    (alias 'compojure 'compojure.core)
    (alter-meta! (r 'compojure/context) assoc :style/indent [:defn])
    (alter-meta! (r 'compojure/GET) assoc :style/indent [:defn])
    (alter-meta! (r 'compojure/PUT) assoc :style/indent [:defn])
    (alter-meta! (r 'compojure/POST) assoc :style/indent [:defn])
    (alter-meta! (r 'compojure/OPTIONS) assoc :style/indent [:defn])
    (alter-meta! (r 'compojure/DELETE) assoc :style/indent [:defn])
    )


  (when (resolve-ns 'taoensso.timbre)
    (alias 'timbre 'taoensso.timbre)
    (alter-meta! (r 'timbre/merge-config!) assoc :style/indent [0])
    (alter-meta! (r 'timbre/swap-config!) assoc :style/indent [:defn])
    (alter-meta! (r 'timbre/fatal) assoc :style/indent [:defn])
    (alter-meta! (r 'timbre/error) assoc :style/indent [:defn])
    (alter-meta! (r 'timbre/warn) assoc :style/indent [:defn])
    (alter-meta! (r 'timbre/info) assoc :style/indent [:defn])
    (alter-meta! (r 'timbre/debug) assoc :style/indent [:defn])
    (alter-meta! (r 'timbre/trace) assoc :style/indent [:defn])
    (alter-meta! (r 'timbre/report) assoc :style/indent [:defn])
    )


  (when (resolve-ns 'aleph.http)
    (alter-meta! (r 'aleph.http/request) assoc :style/indent [0])
    (alter-meta! (r 'aleph.http/get) assoc :style/indent [:defn])
    (alter-meta! (r 'aleph.http/head) assoc :style/indent [:defn])
    (alter-meta! (r 'aleph.http/post) assoc :style/indent [:defn])
    (alter-meta! (r 'aleph.http/put) assoc :style/indent [:defn]))


  (when (resolve-ns 'manifold.deferred)
    (alter-meta! (r 'manifold.deferred/chain) assoc :style/indent [:defn])
    (alter-meta! (r 'manifold.deferred/catch) assoc :style/indent [:defn])
    )
  )
