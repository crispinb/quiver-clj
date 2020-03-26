(ns quiver.core
  (:require [quiver.lib :as lib])
  (:gen-class))

(def valid-notebook "test-data/Valid_notebook_with_two_notes.qvnotebook/")

;; accept notebook fiel path argument  https://github.com/clojure/tools.cli


(defn -main [& args]
  (println (lib/seq-note-metadata (lib/seq-note-paths valid-notebook)))
  )
