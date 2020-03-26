(ns quiver.core (:require
                 [clojure.java.io :as io]
                 [clojure.string :as str]
                 [clojure.data.json :as json]))

(defn get-file-contents [filename]
  (->
   filename
   slurp))

(def valid-notebook "test-data/Valid_notebook_with_two_notes.qvnotebook/")

(defn is-note? [name]
  (str/ends-with? name ".qvnote"))

(defn seq-note-paths [path]
  (map #(str (.getAbsolutePath %1) "/meta.json")
       (filter #(is-note? (.getName %1)) (file-seq (io/file path)))))


(defn seq-note-metadata [note-paths]
  (map json/read-str (map #(get-file-contents %1) note-paths)))

(seq-note-metadata (seq-note-paths valid-notebook))
