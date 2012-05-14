(ns app.server
  (:require [app.routes :as routes]
            [ring.adapter.jetty :as ring]))

(defn start [port]
  (ring/run-jetty #'routes/app {:port (or port 8080) :join? false}))

(defn -main []
  (let [port (Integer. (System/getenv "PORT"))]
    (start port)))