(ns quiver.core (:require [clojure.java.io :as io]))

(defn get-json [filename]
  (->
       ;; best way to test for existence of the resource?
;; and perhaps validate the json?
    io/resource
    slurp))

(get-json "test-file.json")

(.toUpperCase to"fark")
