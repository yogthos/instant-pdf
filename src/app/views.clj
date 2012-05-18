(ns app.views
  (:use [hiccup core page form] clojure.data.json markdown)
  (:require [clj-pdf.core :as pdf]
            [ring.util.response :as response]
            [clojure.java.io :as io])
  (:import [java.io File StringWriter]))


(defn- get-resource-stream [context resource]
  (clojure.java.io/reader 
   (if context
     (.getResourceAsStream context     
      (str File/separator 
           "WEB-INF" File/separator 
           "classes" File/separator  
           "public" File/separator 
           resource))
     (io/resource (str "public/" resource)))))

(defn- read-help [context]  
  (with-open [in (get-resource-stream context "README.md")] 
    (let [out (new StringWriter)]
      (md-to-html in out)
      (.toString out))))

(defn index-page [params]
  (html5
    [:head [:title "Instant PDF"]
     (include-css "http://kevinburke.bitbucket.org/markdowncss/markdown.css"
                  "/css/style.css")]
    [:body
     (form-to [:post "/"] 
              [:p "Enter JSON"]
              (text-area {:rows "40"} "json-input" "")
              [:br]
              (submit-button "Generate PDF"))
     [:br]     
     (read-help (:servlet-context params))]))

(defn generate-pdf [params]
  (try 
    
    (let [doc (read-json (get params "json-input"))]
      
      (with-open [out (new java.io.ByteArrayOutputStream)]
        (pdf/write-doc
          (clojure.walk/prewalk
            #(cond 
               (map? %) 
               (into {} (map (fn [[k v]] [(keyword k) v]) %))
               
               (and (vector? %) (= "image" (first %)))
               (do                 
                 (if (and (map? (second %)) (:base64 (second %)))
                 % (concat (butlast %) [(new java.net.URL (last %))])))
               
               :else %)
            doc) 
          out)
                  
        (with-open [in (new java.io.ByteArrayInputStream (.toByteArray out))]                
          (.flush out)
          
          (-> (response/response in)
            (response/header "Content-Disposition" "filename=document.pdf")
            (response/content-type "application/pdf")
            (response/header "Content-Length" (.size out))) )))
    (catch Exception ex 
      (do
        (.printStackTrace ex)
        {:status 500
         :headers {"Content-Type" "text/html"}
         :body (str "Error occured while parsing the document: " (.getMessage ex))}))))		



