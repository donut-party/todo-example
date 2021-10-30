(ns donut.todo-example.backend.endpoint.todo-list-test
  (:require [clojure.test :refer [deftest is use-fixtures]]
            [donut.endpoint.test.harness :as deth]
            [donut.todo-example.data :as data]))

(use-fixtures :each (deth/system-fixture [:test]))

(deftest gets-todo-lists
  (data/with-test-data
    [{:keys [tl0 tl1]} {:todo-list [[2]]}]
    (is (= [tl0 tl1]
           (deth/read-body (deth/handle-request :get :todo-lists))))))

(deftest gets-single-todo-list
  (data/with-test-data
    [{:keys [tl0]} {:todo-list [[1]]}]
    (is (= [tl0]
           (deth/read-body (deth/handle-request :get [:todo-lists {:id (:todo_list/id tl0)}]))))))

(deftest updates-todo-list
  (data/with-test-data
    [{:keys [tl0]} {:todo-list [[1]]}]
    (is (= [#:todo_list{:id (:todo_list/id tl0),:title "new title"}]
           (-> (deth/handle-request
                :put
                [:todo-list {:id (:todo_list/id tl0)}]
                {:title "new title"})
               (deth/read-body))))))

(deftest deletes-todo-list
  (data/with-test-data
    [{:keys [tl0]} {:todo-list [[1]]}]
    (deth/handle-request :delete [:todo-list {:id (:todo_list/id tl0)}])
    (is (= []
           (deth/read-body (deth/handle-request :get :todo-lists))))))
