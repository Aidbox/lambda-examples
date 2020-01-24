# Aidbox Lambdas Examples

Aidbox Lambda is just pure function in clojure,
which can be loaded into Aidbox and attached specific hook

You can debug this function in standard clojure project, cover with tests, then load into Aidbox.

## Aidit hook

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


## Dev

Lambda.code - should be one pure function definition.
You can develope and test function in project like this - see src/ test/


```
make repl # start repl
make test # run tests

```


