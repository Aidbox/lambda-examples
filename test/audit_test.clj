(ns audit-test
  (:require [audit :as sut]
            [matcho.core :as matcho]
            [clojure.test :refer :all]))

(deftest test-audit

  (def request-example
    {:request
     {:body {:id "pt-3"},
      :headers {:accept "text/yaml",
                :authorization "Bearer ?????",
                :cache-control "max-age=0",
                :cookie "???",
                :host "localhost:8765",
                :origin "http://mybox.com",
                :referer "http://mybox.com/index.ytml",
                :sec-fetch-mode "cors",
                :sec-fetch-site "same-origin",
                :user-agent "Mozilla/5.0"},
      :query-string nil,
      :remote-addr "0:0:0:0:0:0:0:1",
      :request-method :post,
      :resource {:id "pt-3"},
      :scheme :http,
      :uri "/Patient",
      :jwt {:claim "???"}
      :client {:id "srv", :resourceType "Client"}
      :user {:id "admin", :resourceType "User"}},
     :response {:body {:id "pt-3",
                       :meta {:createdAt "2020-01-24T13:34:29.352124Z",
                              :lastUpdated "2020-01-24T13:34:29.352124Z",
                              :versionId "179"},
                       :resourceType "Patient"},
                :status 201}})

  (matcho/match
   (sut/audit request-example)
   {:msg "Audit"
    :uri "/Patient"
    :uid "admin"})


  )

