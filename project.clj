(defproject instant-pdf "0.2.0"
  :description "JSON to PDF service"
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [compojure "1.1.5"]
                 [hiccup "1.0.2"]
                 [cheshire "5.0.1"]
                 [markdown-clj "0.9.19"]
                 [clj-pdf "1.0.5"]                                 
                 [ring-server "0.2.7"]]
  :min-lein-version "2.0.0"
  :plugins [[lein-ring "0.8.2"]] 
  :profiles {:production
             {:ring
              {:open-browser? false 
               :stacktraces? false 
               :auto-reload? false}}
             :dev {:dependencies [[ring-mock "0.1.3"]
                                  [ring/ring-devel "1.1.0"]]}}
  :ring {:handler app.routes/app})
