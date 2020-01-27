(ns policies-test
  (:require [policies :as sut]
            [matcho.core :as matcho]
            [clojure.test :refer :all]))

(deftest test-policies
  (def sample-request
    {:scheme :https
     :request-method :post
     :uri "/Patient"
     :params {:resource/type "Patient"}
     :query-string nil
     :body {:id "pt-5"}
     ;; oauth client
     :client {:auth {:authorization_code {:redirect_uri "/"}}
              :first_party true
              :grant_types ["authorization_code"]
              :id "box-ui"
              :resourceType "Client"
              :source "code"}
     ;; auth user
     :user {:resourceType "User"
            :id "admin"}
     ;; auth jwt
     :jwt {:sub "????"}
     :headers {:accept "application/json"
               :authorization "Bearer MWU0ZTRjNjAtYTQ3My00YTUyLThkNmYtNDg5OGMxYTVhMmQ0"
               :cookie "_ga=GA1.1.735393151.1551431043; _gid=GA1.1.1916383737.1580135484"
               :host "localhost:8765"
               :origin "http://localhost:8765"
               :referer "http://localhost:8765/static/console.html"
               :sec-fetch-mode "cors"
               :sec-fetch-site "same-origin"
               :user-agent "Mozilla/5.0"}
     :remote-addr "0:0:0:0:0:0:0:1"})


  (matcho/match
   (sut/get-policy sample-request)
   {:allow false})


  (matcho/match
   (sut/get-policy {:uri "/Patient" :request-method :get})
   {:allow true})



  )
