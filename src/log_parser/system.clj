(ns log-parser.system
  (:gen-class))

(def system (atom  {:data {:source-file {:resources "upsell1.log"}}
                    :list {:source-file {:resources "coollist.txt"}}
                    :result {:source-file {:resources "result.json"}}}))

(defn get-sys-resources [sys name]
  (get-in sys [name :source-file :resources]))

(comment
  (get-sys-resources @system :data)
  (get-sys-resources @system :list))
