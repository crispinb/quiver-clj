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

(defn seq-note-paths [path]
  (map #(str (.getAbsolutePath %1) "/meta.json")
       (filter #(is-note? (.getName %1)) (file-seq (io/file path)))))


(defn seq-note-metadata [note-paths]
  (map json/read-str (map #(get-file-contents %1) note-paths)))

(defn get-note-titles [notebook-path]
  (let [notes (seq-note-metadata (seq-note-paths notebook-path))]
    (map #(get %1"title") notes)))

(comment
  (def valid-notebook "test-data/Valid_notebook_with_two_notes.qvnotebook/")
  valid-notebook
  (get-note-titles valid-notebook)
  (map :x '({:x "thing" :y "thang"} {:x "sauage"}))
  (get {"x" "sausage"} "x")
  (seq-note-metadata (seq-note-paths valid-notebook)))
