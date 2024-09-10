(ns log-parser.util
  (:require
   [cheshire.core :as json]
   [clojure.string :as str]
   [clojure.java.io :as io]
   [malli.generator :as mg]
   [log-parser.system :as sys]
   [log-parser.entity :as entity])
  (:gen-class))

(defn parse-line [s]
  (-> s
      (str/split  (re-pattern "DATA: "))
      (get 1)
      (json/parse-string true)))

(comment
  (parse-line "1DATA: {}"))

(defn includes-in-str
  [data subs]
  (let [s (get-in data [:props :emailAddress])]
    (some #(str/includes? s %) subs)))

(comment
  (includes-in-str
   {:props {:emailAddress "test@test.com"}}
   ["test"]))

(defn handle-read-file-lines [fname handler]
  (with-open [rdr (io/reader (io/resource fname))]
    (doall (handler (line-seq rdr)))))

(comment
  (let [p (sys/get-sys-resources @sys/system :list)]
    (handle-read-file-lines p identity)))

(defn handle-write-file [txt fname]
  (with-open [wtr (io/writer (io/resource fname))]
    (.write wtr txt)))

(comment
  (->
   (mg/generate entity/Data)
   (json/encode)
   (handle-write-file "test-result.json")))
