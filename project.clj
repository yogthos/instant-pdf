(defproject instant-pdf "0.2.3"
  :description "JSON to PDF service"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [compojure "1.1.8"]
                 [hiccup "1.0.5"]
                 [cheshire "5.3.1"]
                 [markdown-clj "0.9.44"]
                 [clj-pdf "1.11.18"]
                 [ring-server "0.3.1"]]
  :min-lein-version "2.0.0"
  :aot :all
  :plugins [[lein-ring "0.8.7"]]
  :ring {:handler app.routes/app}
  :profiles {:production
             {:ring
              {:open-browser? false
               :stacktraces? false
               :auto-reload? false}}
             :dev {:dependencies [[ring-mock "0.1.3"]
                                  [ring/ring-devel "1.1.0"]]}})
