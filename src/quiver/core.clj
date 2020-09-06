;; TODO: cmd to get note content for a given tag
;; BUG: np crash with no filename (main/titles is null)
;; TODO: add help (does tools.cli offer anything here?)
;; TODO: tests (as per rust verion) (look into that test library, and that deps.edn post)
;; TODO: create an uberjar and run from java??

(ns quiver.core
  (:require [quiver.lib :as lib])
  (:require [clojure.tools.cli :refer [parse-opts]])
  (:gen-class))


(def cmdline-options [["-d" "--notebookdir Notebook directory"]
                      ["-h" "--help"]])

(defn -main [& args]
  (let [opts (parse-opts args cmdline-options)
        titles (lib/note-titles (get-in opts [:options :notebookdir]))]
    (if (seq titles)
      (printl (str "Your titles Sir: \n" (pr-str titles)))
      (println (str "No notes found at that location")))))

(comment
  (let [ valid-notebook "test-data/Valid_notebook_with_two_notes.qvnotebook/" ;
        args (str "-f " valid-notebook)]
    args
    (-main args)))
