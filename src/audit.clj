(ns audit)



(defn audit-debug
  "this hook will dump into your logs request and response content"
  [{request :request response :response}]
  {:request request :response response})


(defn audit
  "this is sample of simple audit hook, which logs uri and user-id"
  [{request :request response :response}]
  (let [uid (get-in request [:user :id])]
    (cond-> 
        {:msg "Audit"
         :uri (:uri request)}
      uid (assoc :uid uid))))
