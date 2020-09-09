(ns quiver.lib (:require
                [clojure.java.io :as io]
                [clojure.string :as str]
                [clojure.data.json :as json]))

(defn- file->string 
  "Simplistically read file as string. Return nil on any exception"
  [filename]
  (try (->
        filename
        slurp)
       (catch Exception _ nil)))

(defn- is-note? [name]
  (str/ends-with? name ".qvnote"))

(defn- note-paths 
  "Returns seq of all notes within notebook"
  [notebook-path]
  (filter #(is-note? (.getName %1)) (file-seq (io/file notebook-path))))

(defn- append-to-path [segment path]
  (str (.getAbsolutePath path) "/" segment))

(defn- note-content-paths [notebook-path]
  (map  (partial append-to-path "/content.json") (note-paths notebook-path)))

(defn- note-metadata-paths [path]
  (map (partial append-to-path "meta.json") (note-paths path)))

(defn- note-metadata [note-metadata-paths]
  (map json/read-str (map #(file->string %1) note-metadata-paths)))

;; needed?
(defn- note-summaries [notebook-path]
  (let [notes (note-metadata (note-metadata-paths notebook-path))]
    (map #(select-keys %1 ["title" "uuid"]) notes)))

(defn- notes-metadata [notebook-path]
  (note-metadata (note-metadata-paths notebook-path)))

;; // get each note, content & metadata into a single data structure.
;; // id by uuid presumably

(comment
  (def valid-notebook "test-data/Valid_notebook_with_two_notes.qvnotebook")
  (def no-notebook "ya/wanker/no")
  valid-notebook
  (note-paths valid-notebook)
  (note-paths no-notebook)
  (note-metadata-paths valid-notebook)
  (note-metadata (note-metadata-paths valid-notebook))
  (note-summaries valid-notebook)
  (note-summaries no-notebook)
  (first (note-metadata (note-metadata-paths valid-notebook)))  
  (get-file-contents valid-notebook)
  (first (note-metadata valid-notebook))
  (note-metadata valid-notebook)
  (note-titles valid-notebook)
  (note-content-paths valid-notebook)
  (println (note-metadata (note-metadata-paths valid-notebook))))
