(ns donut.todo-example.backend.endpoint.todo-test
  (:require
   [clojure.test :refer [deftest is use-fixtures]]
   [donut.endpoint.test.harness :as deth]
   [donut.todo-example.data :as data]
   [donut.datapotato.core :as dc]))

(use-fixtures :each (deth/system-fixture {:system-name :test}))

(deftest gets-todos
  (dc/with-fixtures data/datapotato-db
    (let [{:keys [t0 t1 tl0]} (dc/insert-fixtures dc/*ent-db* {:todo [[2]]})]
      (is (= [t0 t1]
             (deth/read-body (deth/handle-request :get [:todos tl0])))))))

(deftest gets-single-todo
  (dc/with-fixtures data/datapotato-db
    (let [{:keys [t0]} (dc/insert-fixtures dc/*ent-db* {:todo [[1]]})]
      (is (= t0
             (deth/read-body (deth/handle-request :get [:todo t0])))))))


(deftest updates-todo
  (dc/with-fixtures data/datapotato-db
    (let [{:keys [t0]} (dc/with-fixtures dc/*ent-db* {:todo [[1]]})]
      (is (= (assoc t0 :todo/description "new description")
             (-> (deth/handle-request
                  :put
                  [:todo t0]
                  {:description "new description"})
                 (deth/read-body)))))))

(deftest deletes-todo
  (dc/with-fixtures data/datapotato-db
    (let [{:keys [t0 tl0]} (dc/insert-fixtures dc/*ent-db* {:todo [[1]]})]
      (deth/handle-request :delete [:todo t0])
      (is (= []
             (deth/read-body (deth/handle-request :get [:todos tl0])))))))
