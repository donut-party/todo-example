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
   {:get {:parameters {:path [:map [:db/id int?]]}
          :handler    (fn [{:keys [all-params db]}]
                        (let [todo-lists (->> {:select [:*]
                                               :from   [:todo_list]
                                               :where  [:= :id (:db/id all-params)]}
                                              sql/format
                                              (j/query db)
                                              (into []))]
                          {:status  200
                           :headers {"Content-Type" "application/transit+json"}
                           :body    todo-lists}))}}})
