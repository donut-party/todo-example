(ns donut.todo-example.backend.endpoint.todo-list
  (:require [honey.sql :as sql]
            [next.jdbc.sql :as jsql]))

(defn todo-list-by-id
  [db params]
  (->> {:select [:*]
        :from   [:todo_list]
        :where  [:= :id (:id params)]}
       sql/format
       (jsql/query db)
       (first)))

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
                         :body   (todo-list-by-id db all-params)})}

    :put {:parameters {:path [:map [:id int?]]}
          :handler    (fn [{:keys [all-params db]}]
                        (jsql/update! db
                                      :todo_list
                                      (dissoc all-params :id)
                                      (select-keys all-params [:id]))
                        {:status 200
                         :body   (todo-list-by-id db all-params)})}

    :delete {:parameters {:path [:map [:id int?]]}
             :handler (fn [{:keys [all-params db]}]
                        (jsql/delete! db :todo_list (select-keys all-params [:id])))}}})
