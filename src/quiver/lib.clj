(ns quiver.lib (:require
                [clojure.java.io :as io]
                [clojure.string :as str]
                [clojure.walk :as walk]
                [clojure.data.json :as json]))

;; error handling
;; For now - any note file (metadata or content) with an error has the
;; :error key added (with exception as the value).
;; This is easy to check, and somewhat forgiving, in that the note may
;; yet contain some useful data

(defn- file->string 
  "Simplistically read file as string. Return nil on any exception"
  [filename]
  (try (->
        filename
        slurp)
       (catch Exception _ nil)))

(defn- is-note? [name]
  (str/ends-with? name ".qvnote"))

(defn- note-dirs 
  "Returns seq of all note dirs (as Java.io.File) within notebook"
  [notebook-path]
  (filter #(is-note? (.getName %1)) (file-seq (io/file notebook-path))))

(defn- append-to-path [segment path]
  (str (.getAbsolutePath path) "/" segment))

(defn- file->json [f]
  (try 
    (json/read-str (file->string f))
    (catch Exception e {:error e})))

(defn- note-data-dirs
  "Takes seq of Java.io.file representing note dirs.\nReturns seq of vecs, each vec being a tuple of paths of a notes content & meta files"
  [dirs] 
  (map (juxt (partial append-to-path "meta.json") (partial append-to-path "content.json")) dirs))

(defn- parse-notes
  "Takes seq of tuples of note content & metadata filepaths. \nReturns map of notes"
  [note-data]
  ;; mapcat here flattens the json objects for the file metadata & contents into key/val tuples for coalescing into one map. Both original maps have the 'title' key but are otherwise disjoint
  (map #(into {} (mapcat  file->json %)) note-data)
)

(defn load-titles 
  [nbpath]
  (->> nbpath
       (note-dirs)
       (map (partial append-to-path "meta.json"))
       (map #(file->json %))
       (walk/keywordize-keys)
       (map :title)))

(defn load-notes
  "Takes a string path to a Quiver notebook directory, returning a seq of maps
   representing the notes therein.\n
   Returns an empty seq if the notebook doesn't exist or is empty"
  [nbpath]
  (-> nbpath
      note-dirs
      note-data-dirs
      parse-notes
      walk/keywordize-keys))

(comment (def n (note-dirs "Valid_notebook_with_two_notes.qvnotebnook")))
