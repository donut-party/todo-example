(ns donut.todo-example.backend.endpoint.todo-test
  (:require [clojure.test :refer [deftest is use-fixtures]]
            [donut.endpoint.test.harness :as deth]
            [donut.todo-example.data :as data]))

(use-fixtures :each (deth/system-fixture [:test]))

(deftest gets-todos
  (data/with-test-data
    [{:keys [t0 t1]} {:todo [[2]]}]
    (is (= [t0 t1]
           (deth/read-body (deth/handle-request :get :todos))))))

(deftest gets-single-todo
  (data/with-test-data
    [{:keys [t0]} {:todo [[1]]}]
    (is (= t0
           (deth/read-body (deth/handle-request :get [:todo (select-keys t0 [:todo/id])]))))))

(deftest updates-todo
  (data/with-test-data
    [{:keys [t0]} {:todo [[1]]}]
    (is (= (assoc t0 :todo/description "new description")
           (-> (deth/handle-request
                :put
                [:todo (select-keys t0 [:todo/id])]
                {:description "new description"})
               (deth/read-body))))))

(deftest deletes-todo
  (data/with-test-data
    [{:keys [t0]} {:todo [[1]]}]
    (deth/handle-request :delete [:todo (select-keys t0 [:todo/id])])
    (is (= []
           (deth/read-body (deth/handle-request :get :todos))))))
