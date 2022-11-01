(ns user.repl.alpha.util)


(defn current-stack-trace
  []
  (.getStackTrace (Thread/currentThread)))


(defn repl-stack-element?
  [stack-element]
  (and (= "clojure.main$repl" (.getClassName  stack-element))
       (= "doInvoke"          (.getMethodName stack-element))))


(defn in-repl?
  []
  (some repl-stack-element? (current-stack-trace)))
