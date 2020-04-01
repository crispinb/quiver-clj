(ns quiver.lib (:require
                [clojure.java.io :as io]
                [clojure.string :as str]
                [clojure.data.json :as json]))

(defn get-file-contents [filename]
  (->
   filename
   slurp))

(defn is-note? [name]
  (str/ends-with? name ".qvnote"))

(defn note-paths [path]
  (filter #(is-note? (.getName %1)) (file-seq (io/file path))))

;; TODO look for java path append func
(defn append-to-path [segment path]
  (str (.getAbsolutePath path) "/" segment))

(defn note-content-paths [path]
  (map  (partial append-to-path "/content.json") (note-paths path)))

(defn note-metadata-paths [path]
  (map (partial append-to-path "/meta.json") (note-paths path)))

(defn note-metadata [note-metadata-paths]
  (map json/read-str (map #(get-file-contents %1) note-metadata-paths)))

(defn note-titles [notebook-path]
  (let [notes (note-metadata (note-metadata-paths notebook-path))]
    (map #(get %1 "title") notes)))

(defn note-contents [notebook-path]
  ;; TODO
  )

(comment
  (def valid-notebook "test-data/Valid_notebook_with_two_notes.qvnotebook/")
  valid-notebook
  (note-titles valid-notebook)
  (note-content-paths valid-notebook)
  (println (note-metadata (note-metadata-paths valid-notebook))))
