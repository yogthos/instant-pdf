(defproject instant-pdf "0.1.0"
  :description "JSON to PDF service"
  :dependencies [[org.clojure/clojure "1.3.0"]
                 [compojure "1.0.2"]
                 [hiccup "1.0.0-RC2"]
                 [org.clojure/data.json "0.1.1"]
                 [markdown-clj "0.5"]
                 [clj-pdf "0.2.0-SNAPSHOT"]
                 [ring/ring-servlet "1.1.0-RC1"]
       		 [com.lowagie/itext "2.1.7"]]
          		 
  :dev-dependencies [[lein-ring "0.5.0"]]
  :ring {:handler app.routes/app})
