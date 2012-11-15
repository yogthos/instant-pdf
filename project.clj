(defproject instant-pdf "0.2.0"
  :description "JSON to PDF service"
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [compojure "1.1.3"]
                 [hiccup "1.0.1"]
                 [cheshire "4.0.3"]
                 [markdown-clj "0.9.9"]
                 [clj-pdf "1.0.2"]
                 [ring/ring-servlet "1.1.0"]                 
                 [ring/ring-jetty-adapter "1.1.0"]]
  :plugins [[lein-ring "0.7.5"]]       		 
  :ring {:handler app.routes/app}
  :main app.server)
