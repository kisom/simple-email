(defproject simple-email "1.0.2"
  :description "Clojure wrapper around Apache Commons Email"
  :dependencies [[org.clojure/clojure "1.3.0"]
                 [org.apache.commons/commons-email "1.2"]]
  :aot [simple-email.core])