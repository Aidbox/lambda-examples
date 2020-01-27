# Aidbox Lambdas Examples

Aidbox Lambda is just pure function in clojure,
which can be loaded into Aidbox and attached specific hook

You can debug this function in standard clojure project, cover with tests, then load into Aidbox.

## Audit hook

Aidbox call aidbox audit hook after each request
and pass map {request: reqest, response: response :as ctx}.
Your hook should return hash-map with attrs, which will be 
forwarded into aidbox structured logs.

Here is `ctx` example.

```edn
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
                :status 201}}

```

To deploy hook lambda:


```yaml
PUT /Lambda/audit

hook: audit
code: | 
  (defn audit [{request :request response :response}]
    (let [uid (get-in request [:user :id])]
      (cond-> {:uri (:uri request)}
        uid (assoc :uid uid))))

```

Make request and see aidbox logs you should see generated logs like:

`{"ev": "audit", "uri": "....", "uid": "..."}`



## Access Policy clj engine

You can write access policy in clojure using `clj` engine
Body of AccessPolicy.clj should be pure clojure function,
which is passed request object:

```yaml
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
     :remote-addr "0:0:0:0:0:0:0:1"}

```

and should return 
```
{
  ;; allow or not
  :allow boolean
  ;; reason for debug
  :reason string
  ;; structured logs for debug - {"ev": "policy/degub", "key": values }"
  :log {:key value}
  
}
```

How to develop policy with tests see [src/policies.clj] and [test/polcies_test.clj]

```yaml
PUT /AccessPolicy/clj-policy

engine: clj
clj:  |
  (defn mypolicy [req]
    (if (= :get (:request-method req))
      {:allow true
       :reason "Reason why for debug"
       :log {:msg "Data to log"}}
      {:allow false
       :reason "Reason why for debug"
       :log {:msg "Data to log"}}))

```

## Dev

Lambda.code - should be one pure function definition.
You can develope and test function in project like this - see src/ test/


```
make repl # start repl
make test # run tests

```
