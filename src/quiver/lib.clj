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

;; TODO use java path append func
(defn append-to-path [segment path]
  (str (.getAbsolutePath path) "/" segment))

(defn note-content-paths [path]
  (map  (partial append-to-path "/content.json") (note-paths path)))

(defn note-metadata-paths [path]
  (map (partial append-to-path "/meta.json") (note-paths path)))

(defn note-metadata [note-metadata-paths]
  (map json/read-str (map #(get-file-contents %1) note-metadata-paths)))

(defn note-summaries [notebook-path]
  (let [notes (note-metadata (note-metadata-paths notebook-path))]
    (map #(select-keys %1 ["title" "uuid"]) notes)))

(defn notes-metadata [notebook-path]
  (note-metadata (note-metadata-paths notebook-path)))

;; todo -use the new note-summaires instead of note-titles from cmdline
;; then allow selection here by uuid for contents
;; then select bynumber instad (I'll have to assign)
(defn note-contents [notebook-path]
  ;; TODO
  )

(def valid-notebook "test-data/Valid_notebook_with_two_notes.qvnotebook/")
(notes-metadata valid-notebook)
(note-summaries valid-notebook)

(comment
  (println "fark")
  (let [valid-notebook "test-data/Valid_notebook_with_two_notes.qvnotebook/"]
    valid-notebook
    (note-titles valid-notebook)
    (note-content-paths valid-notebook)
    (println (note-metadata (note-metadata-paths valid-notebook)))))
