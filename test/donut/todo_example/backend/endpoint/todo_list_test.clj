(ns donut.todo-example.backend.endpoint.todo-list-test
  (:require [clojure.test :refer [deftest is use-fixtures]]
            [donut.endpoint.test.harness :as deth]))

(use-fixtures :each (deth/system-fixture [:test]))

(deftest gets-todo-lists
  (is (= [{:id 1 :title "new list 1"}
          {:id 2 :title "new list 2"}]
         (deth/read-body (deth/handle-request :get :todo-lists)))))

(deftest gets-todo-list
  (is (= [{:id 1 :title "new list"}]
         (deth/read-body (deth/handle-request :get :todo-list {:id 1})))))
