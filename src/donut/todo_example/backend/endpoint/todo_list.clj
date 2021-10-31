(ns donut.todo-example.backend.endpoint.todo-list
  (:require [honey.sql :as sql]
            [next.jdbc.sql :as jsql]))

(def handlers
  {:collection
   {:get (fn [{:keys [db]}]
           (let [todo-lists (->> {:select [:*]
                                  :from   [:todo_list]}
                                 sql/format
                                 (jsql/query db)
                                 (into []))]
             {:status 200
              :body   todo-lists}))}
   :member
   {:get {:parameters {:path [:map [:id int?]]}
          :handler    (fn [{:keys [all-params db]}]
                        {:status 200
                         :body   (->> {:select [:*]
                                       :from   [:todo_list]
                                       :where  [:= :id (:id all-params)]}
                                      sql/format
                                      (jsql/query db)
                                      (into []))})}

    :put {:parameters {:path [:map [:id int?]]}
          :handler    (fn [{:keys [all-params db]}]
                        (jsql/update! db
                                      :todo_list
                                      (dissoc all-params :id)
                                      (select-keys all-params [:id]))
                        {:status 200
                         :body   (->> {:select [:*]
                                       :from   [:todo_list]
                                       :where  [:= :id (:id all-params)]}
                                      sql/format
                                      (jsql/query db)
                                      (into []))})}

    :delete {:parameters {:path [:map [:id int?]]}
             :handler (fn [{:keys [all-params db]}]
                        (jsql/delete! db :todo_list (select-keys all-params [:id])))}}})
