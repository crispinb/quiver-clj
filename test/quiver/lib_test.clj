(ns quiver.lib-test
  (:require 
   [clojure.java.io :as io]
   [clojure.test :refer :all]
   [quiver.lib :as lib]))

  (def valid-notebook (atom nil))
  (use-fixtures :once 
    (fn [testf] 
      (reset! valid-notebook
              (lib/load-notes (io/resource "Valid_notebook_with_two_notes.qvnotebook"))) 
                        (testf)))

(deftest valid-notes 
  (testing "notes are retrieved"
    (is (= 2 (count @valid-notebook))))
  (testing "basic props"
    (is (= "Valid Note" (:title (first @valid-notebook))))))
  
  (deftest invalid-notes)


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