(ns log-parser.entity
  (:require
   [malli.core :as m]
   [malli.generator :as mg]
   [cheshire.core :as json])
  (:gen-class))

(def Data
  [:map
   [:props
    [:map
     [:firstName :string]
     [:lastName :string]
     [:emailAddress :string]
     [:city :string]
     [:state :string]
     [:address1 :string]
     [:postalCode :string]
     [:phoneNumber :string]]]])

(defn validate-data [data]
  (m/validate Data data))

(comment
  (validate-data
   {:props
    {:firstName "zRhcWt2ee8ii71a1z573Jhsv4Su2o",
     :lastName "R7VUHLs02RY1GBX8lmh7coW6RCxbp",
     :emailAddress "916IPeG7XI8bCgC7kFKXU0p81",
     :city "8P60KRdC8m77c5zv9hDs2vcs",
     :state "49b60FXbcRQNR9G8M91B82Nw52ZBP",
     :address1 "BM5rTK8GicNrT354",
     :postalCode "9onjNw9diy4KLpMF1BqJQ",
     :phoneNumber "e84iB69ih2fA1V86qiK7"}})

  ((json/encode (mg/generate Data)))

  (->
   (mg/generate Data)
   (json/encode)
   (json/decode true))

  (let [j (json/encode (mg/generate Data))]
    (m/validate Data (json/decode j))))

