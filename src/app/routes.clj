(ns app.routes
  (:use compojure.core
        app.views
        [hiccup.middleware :only (wrap-base-url)])
  (:require [compojure.route :as route]
            [compojure.handler :as handler]
            [compojure.response :as response]))

(defroutes main-routes
  (GET "/" req (index-page req true))
  (GET "/md" req (index-page req false))
  (POST "/" [json-input md-input]
        (cond
          json-input (json-to-pdf json-input)
          md-input   (md-to-pdf md-input)
          :else      {:status 400 :body "invalid request"}))
  (route/resources "/")
  (route/not-found "Page not found"))

(def app
  (-> (handler/site main-routes)
      (wrap-base-url)
      (handler/api)))
