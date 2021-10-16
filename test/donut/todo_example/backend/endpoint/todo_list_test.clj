(ns donut.todo-example.backend.endpoint.todo-list-test
  (:require [clojure.test :refer [deftest is]]
            [ring.mock.request :as ring-mock]
            [donut.system :as ds]))

(deftest gets-todo-list
  (let [sys  (ds/start :test)
        resp ((get-in sys [::ds/instances :http :handler])
              {:request-method :get
               :uri            "/api/v1/todo-list/1"
               :headers        {"content-type" "application/transit+json"
                                "accept"       "application/transit+json"}})]
    (is (= [{:id 1 :title "new list"}]
           (:body resp)))
    (ds/start sys)))
