(ns quiver.lib-test
  (:require
   [clojure.java.io :as io]
   [clojure.test :refer :all]
   [quiver.lib :as lib]))

(def valid-notebook "Valid_notebook_with_two_notes.qvnotebook")
(def empty-notebook "Empty.qvnotebook")
(def invalid-content-json-notebook "Has_note_with_invalid_note_json.qvnotebook")
(def invalid-metadata-json-notebook "Has_note_with_invalid_metadata_json.qvnotebook")
(def missing-data-files-notebook "Has_note_with_missing_data_files.qvnotebook")

(deftest valid-notes
(let [nbpath (io/resource valid-notebook)
      nb (lib/load-notes nbpath)]
  (testing "titles are retrieved"
    (is (= 2 (count (lib/load-titles nbpath)))))
  (testing "notes are retrieved"
    (is (= 2 (count nb))))
  (testing "basic props"
    (is (= "Valid Note" (:title (first nb)))))))

(deftest invalid-notes
  (testing "empty note"
    (is (= 0 (count (lib/load-notes (io/resource empty-notebook))))))
  (testing "invalid metadata json note"
    (let [note (lib/load-notes (io/resource invalid-metadata-json-notebook))]
      (is (= 2 (count note)))
      (is (some #(contains? % :error) note))))
  
  (testing "invalid content json note"
    (let [note (lib/load-notes (io/resource invalid-content-json-notebook))]
      (is (= 2 (count note)))
      (is (some #(contains? % :error) note))))
  
  (testing "note with missing files"
    (let [note (lib/load-notes (io/resource missing-data-files-notebook))]
      (is (= 1 (count note)))
      (is (some #(contains? % :error) note)))))


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