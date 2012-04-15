(ns app.routes
  (:use compojure.core
        app.views        
        [hiccup.middleware :only (wrap-base-url)])
  (:require [compojure.route :as route]
            [compojure.handler :as handler]
            [compojure.response :as response]))

(defroutes main-routes
  (GET "/" params (index-page params))
  (POST "/" {params :form-params} (generate-pdf params))                  
  (route/resources "/")
  (route/not-found "Page not found"))

(def app
  (-> (handler/site main-routes)
    (wrap-base-url)
    (handler/api)))
