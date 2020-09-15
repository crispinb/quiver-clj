(ns quiver.checkvist-data)

;; functions for checkvist data format conversion

(comment 
  (def notes [{:comment "// midnight 1 Jan 2019", :created_at 1546300800, :tags ["tag1" "tag2"], :title "Valid Note", :comment2 "// midnight 2 Jan 2019", :updated_at 1546387200, :uuid "1D1DBD5A-BA33-4DAB-84F9-67EE16C7A07A", :cells [{:type "markdown", :data "note data"} {:type "code", :language "rust", :data "let x: String"}]} {:created_at 1525740868, :tags ["linux"], :title "Empty Note", :updated_at 1525831306, :uuid "15F8ABC8-8B81-44CA-97DC-CB5B0D0D41D7", :cells []}])
  ;; 
  ;; note 1 -> 
  ;; "Valid note\n"
  (first notes)
  )