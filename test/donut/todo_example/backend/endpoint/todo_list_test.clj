(ns donut.todo-example.backend.endpoint.todo-list-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as ring-mock]
            [donut.system :as ds]))

(deftest gets-todo-list
  (let [sys  (ds/start :test)
        resp ((get-in sys [::ds/instances :http :handler])
              (ring-mock/request :get "/api/v1/todo-list/1"))]
    (is (= "[]" (:body resp)))
    (ds/start sys)))
