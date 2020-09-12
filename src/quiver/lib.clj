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

(defn- note-dirs 
  "Returns seq of all note dirs (as Java.io.File) within notebook"
  [notebook-path]
  (filter #(is-note? (.getName %1)) (file-seq (io/file notebook-path))))

(defn- append-to-path [segment path]
  (str (.getAbsolutePath path) "/" segment))

(defn- file->json [f]
  (json/read-str (file->string f)))

(defn- note-data-dirs
  "Takes seq of Java.io.file representing note dirs.\nReturns seq of vecs, each vec being a tuple of paths of a notes content & meta files"
  [dirs] 
  (map (juxt (partial append-to-path "meta.json") (partial append-to-path "content.json")) dirs))

(defn- parse-notes
  "Takes seq of tuples of note content & metadata filepaths. \nReturns map of notes"
  [note-data]
  ;; mapcat here returns the json objects for the file metadata & contents as key/val tuples. Both maps have the 'title' key.
  (map #(into {} (mapcat  file->json %)) note-data)
)

(defn load-notes
  "Takes a string path to a Quiver notebook directory, returning a seq of maps
   representing the notes therein.\n
   Returns an empty seq if anything goes wrong (the path doesn't exist, the notes are faulty, etc)"
  [notebook-path]
  (-> notebook-path
      note-dirs
      note-data-dirs
      parse-notes))



(comment
  (def valid-notebook "test-data/Valid_notebook_with_two_notes.qvnotebook")
  (def no-notebook "ya/wanker/no")
  (def empty-notebook "test-data/Empty.qvnotebook")
  (def invalid-note-json "test-data/Has_note_with_invalid_note_json.qvnotebook")
  (def note-with-missing-metadata "test-data/Has_note_with_missing_metadata.qvnotebook")
  (def note-with-zero-content "test-data/Has_note_with_zero_content.qvnotebook")
  (load-notes valid-notebook)
  (load-notes no-notebook)
  (load-notes empty-notebook)
  (load-notes invalid-note-json)
  (load-notes note-with-missing-metadata)
  (load-notes note-with-zero-content)
    )
