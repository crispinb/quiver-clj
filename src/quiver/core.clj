(ns quiver.core (:require [clojure.java.io :as io]))

(defn get-json [filename]
  (->
   filename
   ;; best way to test for existence of the resource?
   ;; and perhaps validate the json?
   io/resource
   slurp))

(get-json "test-file.json")
(get-json "test-data2.json")

