(defproject instant-pdf "0.2.2"
  :description "JSON to PDF service"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [compojure "1.1.5"]
                 [hiccup "1.0.3"]
                 [cheshire "5.2.0"]
                 [markdown-clj "0.9.25"]
                 [clj-pdf "1.10.0"]
                 [ring-server "0.2.8"]]
  :min-lein-version "2.0.0"
  :plugins [[lein-ring "0.8.5"]]
  :ring {:handler app.routes/app}
  :profiles {:production
             {:ring
              {:open-browser? false
               :stacktraces? false
               :auto-reload? false}}
             :dev {:dependencies [[ring-mock "0.1.3"]
                                  [ring/ring-devel "1.1.0"]]}})
