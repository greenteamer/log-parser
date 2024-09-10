(ns log-parser.core
  (:require
   [cheshire.core :as json]
   [malli.core :as m]
   [malli.transform :as mt]
   [log-parser.entity :as entity]
   [log-parser.system :as sys]
   [log-parser.util :as util])
  (:gen-class))

(defn decode-data [x]
  ; decode data and transform it
  (m/decode
   entity/Data x
   (mt/transformer (mt/strip-extra-keys-transformer))))

(defn make-filter-predicate [data subs]
  (and
   (m/validate entity/Data data)
   (util/includes-in-str data subs)))

(defn filter-predicate [subs]
  (fn [data] (make-filter-predicate data subs)))

(def entry-list
  (let [p (sys/get-sys-resources @sys/system :list)]
    (util/handle-read-file-lines p identity)))

(defn make-process-line-trdr [entry-list]
  (comp
   (map util/parse-line)
   (filter (filter-predicate entry-list))
   (map decode-data)))

(def process-line-trdr
  (make-process-line-trdr entry-list))

(def process-line-seq
  (fn [lines]
    (transduce process-line-trdr conj [] lines)))

(comment
  (let [p (sys/get-sys-resources @sys/system :data)]
    (count (util/handle-read-file-lines p process-line-seq))))

(defn finishing [coll]
  (->>
   coll
   (shuffle)
   (take 12)))

;program
(defn program [sys src entries target]
  (let [p (sys/get-sys-resources sys src)
        t (sys/get-sys-resources sys target)]
    (->
     (util/handle-read-file-lines p process-line-seq)
     (finishing)
     (json/encode {:pretty true})
     (util/handle-write-file t))))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (program @sys/system :data :list :result))

