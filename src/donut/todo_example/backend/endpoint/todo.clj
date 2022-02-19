(ns donut.todo-example.backend.endpoint.todo
  (:require [honey.sql :as sql]
            [next.jdbc.sql :as jsql]))

(def parameters
  {:path [:map [:id int?]]})

(defn todo-by-id
  [db params]
  (->> {:select [:*]
        :from   [:todo]
        :where  [:= :id (:id params)]}
       sql/format
       (jsql/query db)
       first))

(def handlers
  {;; "/todo"
   :collection
   {:get
    (fn [{:keys [db]}]
      {:status 200
       :body   (->> {:select [:*]
                     :from   [:todo]}
                    sql/format
                    (jsql/query db)
                    (into []))})}

   ;; "/todo/:id"
   :member
   {:get
    {:parameters parameters
     :handler    (fn [{:keys [all-params db]}]
                   {:status 200
                    :body   (todo-by-id db all-params)})}

    :put
    {:parameters parameters
     :handler    (fn [{:keys [all-params db]}]
                   (jsql/update! db
                                 :todo
                                 (dissoc all-params :id)
                                 (select-keys all-params [:id]))
                   {:status 200
                    :body   (todo-by-id db all-params)})}

    :delete
    {:parameters parameters
     :handler    (fn [{:keys [all-params db]}]
                   (jsql/delete! db :todo (select-keys all-params [:id])))}}})
