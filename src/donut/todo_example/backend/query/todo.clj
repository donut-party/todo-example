(ns donut.todo-example.backend.query.todo
  (:require [honey.sql :as sql]
            [next.jdbc.sql :as jsql]))

(defn todos-by-todo-list-id
  [db {:keys [:todo_list/id]}]
  (->> {:select [:*]
        :from   [:todo]
        :where  [:= :todo/todo_list_id id]}
       sql/format
       (jsql/query db)
       (into [])))

(defn todo-by-id
  [db {:keys [:todo/id]}]
  (->> {:select [:*]
        :from   [:todo]
        :where  [:= :todo/id id]}
       sql/format
       (jsql/query db)
       first))
