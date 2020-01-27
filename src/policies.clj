(ns policies)


(defn get-policy [req]
  (if (= :get (:request-method req))
    {:allow true}
    {:allow false :reason "only GET requests"}))
