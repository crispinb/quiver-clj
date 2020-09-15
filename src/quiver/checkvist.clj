(ns quiver.checkvist
  (:require [clj-http.client :as client]
            [lambdaisland.uri :as uri]
            [clojure.data.json :as json]
            [clojure.string :as str]))

(def base-url "https://checkvist.com")
(def get-token-url-seg "auth/login.json?version=2")
(def checklists-url-seg "checklists")
(def tasks-url-seg "tasks.json")
(def import-url-seg "import.json")

;; TODO: store data of last store/refresh
;; refresh if > 1day < 90 days
;; refetch if > 90 days
(defonce creds (atom nil))

;; checkvist API is very unreliable, so we rely on repeated quick retries
(def connection-defaults {:accept :json
                          :cookie-policy :none
                          :conn-timeout 5000
                          :headers {"X-Client-Token" @creds}
                          :content-type :json
                          :debug true :debug-body true
                          :retry-handler (fn [_ try-count _]
                                           (println "retrying connection ...")
                                           (< try-count 10))})

(defn- build-api-url [& segs]
  (str (assoc (uri/uri base-url)
              :path (str/join "/" (cons "" segs)))))

(defn- set-auth-token
  "Puts auth token in creds atom. Returns nil to avoid leaking the token"
  [username key]
  (-> (client/get  (build-api-url get-token-url-seg)
                   (merge connection-defaults {:query-params {"username" username "remote_key" key}}))
      (:body)
      (json/read-str)
      (get "token")
      (#(reset! creds %)))
  nil)

;; don't really need this - it's just the simplest call to test the checkvist API with
(defn- get-list-items [list-id]
  (let [url (build-api-url checklists-url-seg list-id tasks-url-seg)]
    (client/get url
                connection-defaults)))

(defn- create-task [list-id task-data]
  (let [url (build-api-url checklists-url-seg list-id tasks-url-seg)]
    (client/post url (merge connection-defaults {:body (json/write-str task-data)}))))

;; import hierarchy of tasks
(defn- import-tasks [list-id tasks-data]
  (let [url (build-api-url  checklists-url-seg list-id import-url-seg)]
    (client/post url
                 (merge connection-defaults
                        {:body (json/write-str (merge
                                               ;; TODO: add import options here for lines, attachments etc
                                                {}
                                                {:import_content tasks-data}))}))))

(comment
  (get-list-items "774394")
  (def task-data {"content" "tested from clojure" "tags" {"#dev" false} "tags_as_text" "dev"})
  (def import-data "Top-level-item\n  sub item 1/1\n  sub item 1/2\n    sub item 2/1")
  (create-task "774394" task-data)
  (import-tasks "774394" import-data)
  (json/write-str task-data))
