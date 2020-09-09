;; TODO: compile base list of features for project kanban
;; TODO: cmd to get note content for a given tag
;; BUG: np crash with no filename (main/titles is null)
;; TODO: add help (does tools.cli offer anything here?)
;; TODO: tests (as per rust verion) (look into that test library, and that deps.edn post)
;; TODO: create an uberjar and run from java??
;; TODO: babashka for cmdline version?
;; TODO: start with tests for library once have a real plan outnutted

(ns quiver.core
  (:require [quiver.lib :as lib])
  (:require [clojure.tools.cli :refer [parse-opts]])
  (:gen-class))


(def cmdline-options [["-d" "--notebookdir Notebook directory"]
                      ["-h" "--help"]])

(defn -main [& args]
  (println "received args " args)
  (let [opts (parse-opts args cmdline-options)]
    (println "opts: " opts)
    (lib/note-titles (get-in opts [:options :notebookdir]))))
    ; (if (seq titles)
    ;   (println (str "Your titles Sir: \n" (pr-str titles)))
    ;   (println (str "No notes found at that location")))))

(comment
  (def valid-n "test-data/Valid_notebook_with_two_notes.qvnotebook/")
  valid-n
  (-main "-d" valid-n))
