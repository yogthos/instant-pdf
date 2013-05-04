(ns app.views
  (:use [hiccup core page form] cheshire.core markdown.core)
  (:require [clj-pdf.core :as pdf]
            [ring.util.response :as response]
            [clojure.java.io :as io])
  (:import [java.io File StringWriter]
           org.apache.commons.io.IOUtils))

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
              (text-area 
                {:rows "30"} 
                "json-input" "[{\"title\":\"My document\"}, \"some content here...\"]")
              [:br]
              (submit-button "Generate PDF"))
     [:br]     
     (read-help (:servlet-context params))]))

(defn generate-pdf [params]
  (try 
    
    (let [doc (parse-string (get params "json-input") true)]
      
      (with-open [out (new java.io.ByteArrayOutputStream)]
        (pdf/write-doc doc out)
                  
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
         :body (html5 [:body [:h2 "An error has occured while parsing the document"] (.getMessage ex)])}))))		



(def recset [["baz" "quux"]["wib" "wob"]])

(def f ['$foo '$bar])
(def q (pdf/template f))

(defmacro template* [f]
  `(vec ~f))

(macroexpand '(template* f))

(template* f)

(macroexpand '((pdf/template (template* f)) recset))

((pdf/template ~f) recset)
