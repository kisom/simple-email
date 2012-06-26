(ns simple-email.core
  (:import [org.apache.commons.mail SimpleEmail]))

(defn mail-server
  "Set up a mail server."
  [mail-host mail-port mail-ssl mail-user mail-pass mail-from]
  (fn [recipients subject message]
    (let [email (SimpleEmail.)
          mail-port (str mail-port)]
     (do
       (.setHostName email mail-host)
       (.setSslSmtpPort email mail-port)
       (.setSmtpPort email (Integer. mail-port))
       (.setTLS email mail-ssl)
       (doseq [recipient recipients]
          (.addTo email recipient))
       (.setFrom email mail-from)
       (.setSubject email subject)
       (.setMsg email message)
       (.setAuthentication email mail-user mail-pass)
       (.send email)))))

(defn prefix-env
  "Short cut to easily prefix an environment variable."
  [prefix env-var]
  (format "%s_%s" prefix env-var))

(defn send-to
  "Send an email to a single recipient."
  [server recipient subject message]
  (apply (eval server) [[recipient] subject message]))
