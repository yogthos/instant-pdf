(ns app.server
  (:require [app.routes :as routes]
            [ring.adapter.jetty :as ring])
  (:gen-class))

(defn -main [& [port]]  
  (ring/run-jetty #'routes/app {:port (if port (Integer/parseInt port) 8080) 
                                :join? false}))