(ns quiver.core
  (:require [quiver.lib :as lib])
  (:require [clojure.tools.cli :refer [parse-opts]])
  (:require [clojure.java.io :as io])
  (:gen-class))


(def cmdline-options [["-d" "--notebookdir Notebook directory"]
                      ["-h" "--help"]])

(defn -main [& args]
  (println "received args " args)
  (let [opts (parse-opts args cmdline-options)]
    (println "opts: " opts)
    (lib/load-notes (get-in opts [:options :notebookdir]))))

(comment
  (def valid-n (io/resource "Valid_notebook_with_two_notes.qvnotebook/"))
  (def big-n "/mnt/c/Users/Crispin Bennett/cbfiles/archive/notes/Quiver - original format/cbnotes.qvlibrary/6F4825BD-0088-4CE3-8B29-FC8112EE0F88.qvnotebook")
  (-main "-d" valid-n)
  (def r (-main "-d" big-n))
  (println (count r) "first: " (nth r 110)))
