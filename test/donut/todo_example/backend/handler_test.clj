(ns donut.todo-example.backend.handler-test
  (:require [clojure.test :refer :all]
            [donut.todo-example.backend.handler :as sut]
            [ring.mock.request :as mock]))

(deftest router-test
  (= (sut/router (mock/request :get "/api/v1/todo"))
     {:status 200
      :body   "hi"}))
