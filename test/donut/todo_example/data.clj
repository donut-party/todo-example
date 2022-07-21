(ns donut.todo-example.data
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as sg]
            [donut.datapotato.fixtures.next-jdbc :as dfn]
            [donut.endpoint.test.harness :as deth]
            [donut.todo-example.backend.system] ;; for multimethod
            [next.jdbc :as jdbc]))

(defn db-connection
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
               :fixtures  {:table-name "todo"}
               :relations {:todo_list_id [:todo-list :todo_list/id]}}
   :todo-list {:prefix   :tl
               :generate {:schema :todo-list/entity}
               :fixtures {:table-name "todo_list"}}})

(def table-names
  {:todo      :todo
   :todo-list :todo_list})


(def datapotato-db
  {:schema   dd-schema
   :generate {:generator (comp sg/generate s/gen)}
   :fixtures (merge dfn/config
                    {:get-connection (fn [_] (db-connection))
                     :setup          (fn [{{:keys [connection]} :fixtures}]
                                       (jdbc/execute! connection ["TRUNCATE TABLE todo CASCADE"])
                                       (jdbc/execute! connection ["TRUNCATE TABLE todo_list CASCADE"]))})})
