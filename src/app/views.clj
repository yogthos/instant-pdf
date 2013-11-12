(ns app.views
  (:use [hiccup core page form] cheshire.core markdown.core)
  (:require [clj-pdf.core :as pdf]
            [app.md :as md]
            [ring.util.response :as response]
            [clojure.java.io :as io])
  (:import [java.io File StringWriter]
           org.apache.commons.io.IOUtils))


(defn- read-help []
  (with-open [in  (-> "public/README.md" io/resource io/input-stream)
              out (new StringWriter)]
    (md-to-html in out)
    (.toString out)))

(defn index-page [req]
  (html5
    [:head [:title "Instant PDF"]
     (include-css "/css/style.css")]
    [:body
     (form-to [:post "/"]
              [:h1 "Enter JSON"]
              (text-area {:rows "30"}
                "json-input" "[{\"title\":\"My document\"}, \"some content here...\"]")
              [:br]
              (submit-button {:class "button"} "Generate PDF"))
     [:br]
     (read-help)]))

(defn generate-pdf [parser]
  (try
    (with-open [out (new java.io.ByteArrayOutputStream)]
        (parser out)

        (with-open [in (new java.io.ByteArrayInputStream (.toByteArray out))]
          (.flush out)

          (-> (response/response in)
            (response/header "Content-Disposition" "filename=document.pdf")
            (response/content-type "application/pdf")
            (response/header "Content-Length" (.size out))) ))
    (catch Exception ex
      (do
        (.printStackTrace ex)
        {:status 500
         :headers {"Content-Type" "text/html"}
         :body (html5 [:body [:h2 "An error has occured while parsing the document"] (.getMessage ex)])}))))

(defn md-to-pdf [md-input]
  (generate-pdf (partial md/md-to-pdf md-input)))

(defn json-to-pdf [json-input]
  (generate-pdf (partial pdf/write-doc (parse-string json-input true))))
