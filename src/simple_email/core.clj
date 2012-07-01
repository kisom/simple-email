(ns simple-email.core
  (:import [org.apache.commons.mail SimpleEmail]))

(defn- prefix-env
  "Short cut to easily prefix an environment variable."
  [prefix env-var]
  (let [prefix (if (not (empty? prefix)) (format "%s_" prefix) "")]
    (System/getenv
     (format "%s%s" prefix env-var))))

(defn- parse-bool
  "Parse a bool from a string."
  [bool-string]
  (let [bool-string (.toLowerCase (str bool-string))]
    (cond (= bool-string "yes") true
          (= bool-string "true") true
          (= bool-string "1") true
          true false)))

(defn- send-message
  "Send an email, catching errors."
  [server recipients subject message]
  (try
    (apply (eval server) [recipients subject message])
    (hash-map :ok true :message nil)
    (catch Exception e
      (let [cause (.getCause e)
            cause (if (nil? cause) nil (.toString cause))]
        (hash-map :ok false
                  :message (.getMessage e)
                  :cause cause)))))

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

(defn mail-server-from-env
  "Set up a mail server with environment variables."
  [& args]
  (let [prefix (if (> (count args) 0) (first args) "")
        mail-host (prefix-env prefix "MAIL_HOST")
        mail-port (prefix-env prefix "MAIL_PORT")
        mail-ssl  (parse-bool (prefix-env prefix "MAIL_SSL"))
        mail-user (prefix-env prefix "MAIL_USER")
        mail-pass (prefix-env prefix "MAIL_PASS")
        mail-from (prefix-env prefix "MAIL_FROM")]
    (mail-server mail-host mail-port mail-ssl mail-user mail-pass mail-from)))

(defn send-to
  "Synchronously send an email to a single recipient."
  ([agent-state server recipient subject message]
     (println "state passed in")
     (send-message server [recipient] subject message))
  ([server recipient subject message]
     (if (not (string? recipient))
       (hash-map :ok false
                 :message "Invalid recipient."
                 :cause "Recipient should be a string with a single address.")
       (send-message server [recipient] subject message))))

(defn send-mail
  "Synchronously send an email to a list of recipients."
  ([agent-state server recipients subject message]
     (send-message server recipients subject message))
  ([server recipients subject message]
     (if (and
          (not (vector? recipients))
          (not (vector? recipients)))
       (hash-map :ok false
                 :message "Invalid recipients."
                 :cause "Recipients should be a vector or list of addresses.")
       (send-message server recipients subject message))))

(defn send-to-async
  "Asynchronously send an email to a single recipient."
  [server recipient subject message]
  (let [mail-agent (agent {})]
    (send mail-agent send-to server recipient subject message)))

(defn send-mail-async
  "Asynchronously send email to a list of recipients."
  [server recipient subject message]
  (let [mail-agent (agent {})]
    (send mail-agent send-mail server recipient subject message)))