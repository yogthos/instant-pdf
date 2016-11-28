(defproject instant-pdf "0.2.3"
  :description "JSON to PDF service"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [compojure "1.5.1"]
                 [hiccup "1.0.5"]
                 [cheshire "5.6.3"]
                 [markdown-clj "0.9.91"]
                 [clj-pdf "2.2.8"]
                 [ring-server "0.4.0"]]
  :min-lein-version "2.0.0"
  :aot :all
  :uberjar-name "instant-pdf.jar"
  :plugins [[lein-ring "0.9.7"]]
  :ring {:handler app.routes/app
         :uberwar-name "instant-pdf.war"}
  :profiles {:production
             {:ring
              {:open-browser? false
               :stacktraces? false
               :auto-reload? false}}})
