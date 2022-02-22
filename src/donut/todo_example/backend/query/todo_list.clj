(ns donut.todo-example.backend.query.todo-list
  (:require [honey.sql :as sql]
            [next.jdbc.sql :as jsql]))

(defn todo-list-by-id
  [db {:keys [:todo_list/id]}]
  (->> {:select [:*]
        :from   [:todo_list]
        :where  [:= :todo_list/id id]}
       sql/format
       (jsql/query db)
       (first)))

(defn todo-lists
  [db]
  (->> {:select [:*]
        :from   [:todo_list]}
       sql/format
       (jsql/query db)
       (into [])))
