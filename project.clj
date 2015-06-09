(defproject instant-pdf "0.2.3"
  :description "JSON to PDF service"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [compojure "1.3.4"]
                 [hiccup "1.0.5"]
                 [cheshire "5.5.0"]
                 [markdown-clj "0.9.66"]
                 [clj-pdf "2.0.4"]
                 [ring-server "0.4.0"]]
  :min-lein-version "2.0.0"
  :aot :all
  :plugins [[lein-ring "0.8.7"]]
  :ring {:handler app.routes/app}
  :profiles {:production
             {:ring
              {:open-browser? false
               :stacktraces? false
               :auto-reload? false}}})
