(ns donut.todo-example.backend.endpoint.todo-list
  (:require [clojure.java.jdbc :as j]
            [honey.sql :as sql]))

(def handlers
  {:collection
   {:get (fn [{:keys [db]}]
           (let [todo-lists (->> {:select [:*]
                                  :from   [:todo_list]}
                                 sql/format
                                 (j/query db)
                                 (into []))]
             {:status  200
              :headers {"Content-Type" "text/html"}
              :body    (str todo-lists)}))}
   :member
   {:get (fn [{:keys [path-params db] :as req}]
           (let [todo-lists (->> {:select [:*]
                                  :from   [:todo_list]
                                  :where  [:= :id (java.lang.Integer/parseInt (:db/id path-params))]}
                                 sql/format
                                 (j/query db)
                                 (into []))]
             {:status  200
              :headers {"Content-Type" "text/html"}
              :body    (str todo-lists)}))}})
