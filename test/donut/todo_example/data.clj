(ns donut.todo-example.data
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as sg]
            [donut.datapotato.insert.next-jdbc :as ddin]
            [donut.endpoint.test.harness :as deth]
            [donut.todo-example.backend.system] ;; for multimethod
            [next.jdbc.sql :as jsql]
            [next.jdbc :as jdbc]))

(defn db
  []
  (deth/instance [:db :connection]))

(s/def :todo-list/title string?)

(s/def :todo-list/entity
  (s/keys :req-un [:todo-list/title]))


(s/def :todo/todo_list_id int?)
(s/def :todo/description string?)
(s/def :todo/done boolean?)

(s/def :todo/entity
  (s/keys :req-un [:todo/todo_list_id
                   :todo/description
                   :todo/done]))

(def dd-schema
  {:todo      {:prefix    :t
               :generate  {:schema :todo/entity}
               :insert    {:table-name "todo"}
               :relations {:todo_list_id [:todo-list :todo_list/id]}}
   :todo-list {:prefix   :tl
               :generate {:schema :todo-list/entity}
               :insert   {:table-name "todo_list"}}})

(def table-names
  {:todo      :todo
   :todo-list :todo_list})


(defn perform-insert
  [_ent-db {:keys [ent-type visit-val]}]
  (jsql/insert! (db)
                (ent-type table-names)
                visit-val))

(def datapotato-db
  {:schema   dd-schema
   :generate {:generator (comp sg/generate s/gen)}
   :insert   {:get-insert-db (fn [] (db))
              :get-inserted  (fn [{:keys [insert-result]}]
                               insert-result)}})


(defn truncate-all
  []
  (jdbc/execute! (db) ["TRUNCATE TABLE todo CASCADE"])
  (jdbc/execute! (db) ["TRUNCATE TABLE todo_list CASCADE"]))

(defmacro with-test-data
  [[binding-names query] & body]
  `(do
     (truncate-all)
     (let [~binding-names (ddin/generate-insert datapotato-db ~query)]
       ~@body)))
