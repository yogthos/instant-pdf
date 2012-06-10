(defproject instant-pdf "0.2.0"
  :description "JSON to PDF service"
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [compojure "1.0.4"]
                 [hiccup "1.0.0"]
                 [org.clojure/data.json "0.1.2"]
                 [markdown-clj "0.8"]
                 [clj-pdf "0.9.0"]
                 [ring/ring-servlet "1.1.0"]
       		 [com.lowagie/itext "2.1.7"]
                 [ring/ring-jetty-adapter "1.1.0"]]
  :dev-dependencies [[lein-ring "0.7.0"]]          		 
  :ring {:handler app.routes/app})
