(ns donut.todo-example.backend.endpoint.todo-list-test
  (:require [clojure.test :refer [deftest is use-fixtures]]
            [donut.endpoint.test.harness :as deth]))

(use-fixtures :each (deth/system-fixture :test))

(deftest gets-todo-list
  (is (= [{:id 1 :title "new list"}]
         (deth/read-body (deth/req :get :todo-list {:id 1})))))
