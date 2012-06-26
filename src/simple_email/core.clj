(ns simple-email.core
  (:import [org.apache.commons.mail SimpleEmail]))

(defn mail-server
  "Set up a mail server."
  [mail-host mail-port mail-ssl mail-user mail-pass mail-from]
  (fn [recipients subject message]
    (doto (SimpleEmail.)
      (.setHostName mail-host)
      ((if mail-ssl
          (.setSslSmtpPort (Integer. mail-port))
          (.setSmtpPort (Integer. mail-port))))
      (.setSSL mail-ssl)
      ((doseq [recipient recipients]
          (.addTo recipient)))
      (.setFrom mail-from)
      (.setSubject subject)
      (.setMsg message)
      (.setAuthentication mail-user mail-pass)
      (.send))))

(defn prefix-env
  "Short cut to easily prefix an environment variable."
  [prefix env-var]
  (format "%s_%s" prefix env-var))

(defn send-to
  "Send an email to a single recipient."
  [server recipient subject message]
  (apply server [recipient] subject message))
