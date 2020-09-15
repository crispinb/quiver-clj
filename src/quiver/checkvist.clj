(ns quiver.checkvist
  (:require [clj-http.client :as client]
            [lambdaisland.uri :as uri]
            [clojure.data.json :as json]
            [clojure.string :as str]))

(def base-url "https://checkvist.com")
(def get-token-url-seg "auth/login.json?version=2")
(def checklists-url-seg "checklists")
(def tasks-url-seg "tasks.json")
;; checkvist API is very unreliable, so we rely on a small number of quick retries
(def connection-defaults {:accept :json
                          :cookie-policy :none
                          :conn-timeout 5000
                          :headers {"X-Client-Token" @creds}
                          :content-type :json
                          :debug true :debug-body true
                          :retry-handler (fn [ex try-count http-context]
                                           (println "retrying connection ..." )
                                           (< try-count 5))})
(defonce creds (atom nil))

;; TODO: checkvist api times out a lot. Strategy needed (does clj-http have anything useful like retries?)
(defn- get-auth-token [username key]
  (-> (client/get (str (uri/join base-url get-token-url-seg))
                  {:accept :json
                   :cookie-policy :none
                   ;; checkvist API can be very slow
                   :conn-timeout 30000
                   :query-params {"username" username "remote_key" key}})
      (:body)
      (json/read-str)
      (get "token")))

;; don't really need this - it's just the simplest call to test the checkvist API with
(defn- get-list-items [list-id]
  (let [url (str (assoc (uri/uri base-url)
                        :path (str/join "/" [nil checklists-url-seg, list-id, tasks-url-seg])))]
    (println "calling " url)
    (client/get url
                {:accept :json
                 :cookie-policy :none
                 :headers {"X-Client-Token" @creds}})))

(defn- create-task [list-id task-data]
  (let [url (str (assoc (uri/uri base-url)
                        :path (str/join "/"  [nil checklists-url-seg list-id tasks-url-seg])))]
    (println "calling " url)
    (client/post url (merge connection-defaults {:body (json/write-str task-data)}))))


;; import hierarchy of tasks
(defn- import-tasks [list-id tasks])

;; TODO: is it possible it's just the checkvist api? First call from Postman took 20s; about 500ms thereafter
;; I also just had it time out at the console
;; maybe the checkvist calls & the maven thing are entirely separate issues
;; 
;; Is there anything in the google groups (I think that's their support channel for the API) about this?

(comment
  (def pc-url "https://checkvist.com/p/GAjsxexcncCF74pqR6aPZq/tasks.json")
  (def g (client/get pc-url))
  (client/get "https://checkvist.com/checklists/774394/auth/login.json?version=2")
  (client/get "https://checkvist.com/auth/login.json?version=2&username=myself@crisbennett.com&remote_key=k9LPfkvwnjbIOY")

  (reset! creds (get-auth-token  "myself@crisbennett.com" "k9LPfkvwnjbIOY"))
  (get-list-items "774394")
  (def task-data {"content" "tested from clojure" "tags" {"#dev" false} "tags_as_text" "dev"})
  (create-task "774394" task-data)
  (json/write-str task-data))