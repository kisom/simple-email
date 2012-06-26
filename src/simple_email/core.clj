(ns simple-email.core
  (:import [org.apache.commons.email SimpleEmail]))

(defn mail-server
  [mail-host mail-port mail-ssl mail-user mail-pass mail-from]
  (fn [recipients subject message]
    (doto (SimpleEmail.)
      (.setHostName mail-host)
      (if mail-ssl
        (.setSslSmtpPort (Integer. mail-port))
        (.setSmtpPort (Integer. mail-port)))
      (.setSSL mail-ssl)
      (doseq [recipient recipients]
        (.addTo recipient))
      (.setFrom mail-from)
      (.setSubject subject)
      (.setMsg message)
      (.setAuthentication mail-user mail-pass)
      (.send))))
