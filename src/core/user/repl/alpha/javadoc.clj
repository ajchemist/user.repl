(ns user.repl.alpha.javadoc
  (:require
   [clojure.java.io :as jio]
   [clojure.java.javadoc :as javadoc]
   ))


(def ^:dynamic *core-java-api*
  (case (System/getProperty "java.specification.version")
    ("1.8" "8") "http://docs.oracle.com/javase/8/docs/api/"
    "9"         "http://docs.oracle.com/javase/9/docs/api/"
    "10"        "http://docs.oracle.com/javase/10/docs/api/"
    "11"        "https://docs.oracle.com/en/java/javase/11/docs/api/"
    "17"        "https://docs.oracle.com/en/java/javase/17/docs/api/"
    "https://docs.oracle.com/en/java/javase/17/docs/api/"))


;; patch core-java-api url in remote-javadoc


(dosync
  (commute
    javadoc/*remote-javadocs*
    merge
    {"java."          *core-java-api*
     "javax."         *core-java-api*
     "org.ietf.jgss." *core-java-api*
     "org.omg."       *core-java-api*
     "org.w3c.dom."   *core-java-api*
     "org.xml.sax."   *core-java-api*}))


(defn install-remote-javadoc
  ([]
   ;; repl sugar interface
   (install-remote-javadoc nil))
  ([remote-javadoc]
   (run!
     (fn [[prefix url]]
       (javadoc/add-remote-javadoc prefix url))
     remote-javadoc)))
