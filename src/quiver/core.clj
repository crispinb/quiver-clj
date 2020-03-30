;; TODO: cmd to get list of note titles
;; TODO; cmdline help/options for   that cmd
;; TODO: return an error if the path's not a notebook
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
  (def opts (parse-opts args cmdline-options))
  (def titles (lib/get-note-titles (get-in opts [:options :notebookdir])))
  (println (str "your titles Sir: \n" (pr-str titles)))
  )
