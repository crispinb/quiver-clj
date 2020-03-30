;; TODO: cmd to get note content for a given tag
;; TODO: tests (as per rust verion) (look into that test library, and that deps.edn post)
;; TODO: create an uberjar and run from java??

(ns quiver.core
  (:require [quiver.lib :as lib])
  (:require [clojure.tools.cli :refer [parse-opts]])
  (:gen-class))

(def valid-notebook "test-data/Valid_notebook_with_two_notes.qvnotebook/")

(def cmdline-options [["-d" "--notebookdir Notebook directory"]
                      ["-h" "--help"]])

(defn -main [& args]
  (let [opts (parse-opts args cmdline-options)
        titles (lib/get-note-titles (get-in opts [:options :notebookdir]))]
    (if (seq titles)
      (println (str "Your titles Sir: \n" (pr-str titles)))
      (println (str "No notes found at that location")))))
