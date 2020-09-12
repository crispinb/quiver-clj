(ns quiver.lib-test
  (:require 
   [clojure.java.io :as io]
   [clojure.test :refer :all]
   [quiver.lib :refer :all]))

  (def valid-notebook "Valid_notebook_with_two_notes.qvnotebook")

(deftest test_test 
  (testing "valid note"
    (let [n (load-notes (io/resource valid-notebook))]
      (is (= (count n) 2))
      (is (= "Valid note" ((first n) "title")))
      (println n))
    ))


(comment
  (def no-notebook "ya/wanker/no")
  (def empty-notebook "test-resources/Empty.qvnotebook")
  (def invalid-note-json "test-resources/Has_note_with_invalid_note_json.qvnotebook")
  (def note-with-missing-metadata "test-resources/Has_note_with_missing_metadata.qvnotebook")
  (def note-with-zero-content "test-resources/Has_note_with_zero_content.qvnotebook")
  (load-notes valid-notebook)
  (load-notes no-notebook)
  (load-notes empty-notebook)
  (load-notes invalid-note-json)
  (load-notes note-with-missing-metadata)
  (load-notes note-with-zero-content))