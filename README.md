# simple-email

`simple-email` is a Clojure wrapper around the Apache Commons Email.

Right now it only supports sending plaintext emails; in the future, it
may be extended to support HTML emails (but I hate HTML email so probably
not ;)).

## Usage

In `projects.clj` add `[simple-email "1.0.3"]` to the `:dependencies`.

```clojure
(use [simple-email.core])

(def mail-host "mail.dropsonde.net")
(def mail-port 587)
(def mail-user "pollard")
(def mail-pass "rickson")
(def mail-from "pollard@dropsonde.net")
(def mail-ssl true)

(def dropsonde-server
     (mail-server
          mail-host
          mail-port
          mail-ssl
          mail-user
          mail-pass
          mail-from))

(send-to dropsonde-server
         "boone@dropsonde.net" 
         "Witty Subject Line" 
         "Your email body goes here.")
{:ok true :message nil :cause nil}

(send-mail [ "pollard@dropsonde.net" "bigend@drosonde.net" ]
           "This is an email."
           "Further explanation goes here.")
{:ok true :message nil :cause nil}
```

The `send-to` and `send-mail` forms are synchronous; the forms `send-to-async` and
`send-mail-async` will return an agent that can be queried for its status. The result
from the agent or the synchronous functions is a hash-map with the following keys:

* `:ok`: true if the mail was successfully sent or false if there was an exception
* `:message`: the message from the exception on error, nil on success
* `:cause`: the cause of the exception on error, nil on success

simple-email was inspired by [William Groppe's article](http://will.groppe.us/)
["Sending email from Clojure"](http://will.groppe.us/post/406065542/sending-email-from-clojure).

## Using environment variables to set up the mailserver
The form `mail-server-from-env` will use environment variables to determine
the appropriate information for the mailserver. The relevant environment 
variables are

```
MAIL_HOST
MAIL_PORT
MAIL_SSL
MAIL_USER
MAIL_PASS
MAIL_FROM
```

Alternatively, the variables may be prefixed and that prefix should be passed
to the `mail-server-from-env` form. For example, if the environment variables
are named `PREFIX_MAIL_HOST` etc..., you would call

```
(mail-server-from-env "PREFIX")
```


## License

`simple-email` is released under the ISC license.

```
The ISC license:
Copyright (c) 2011 Kyle Isom <coder@kyleisom.net>

Permission to use, copy, modify, and distribute this software for any
purpose with or without fee is hereby granted, provided that the above 
copyright notice and this permission notice appear in all copies.

THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE. 

```
