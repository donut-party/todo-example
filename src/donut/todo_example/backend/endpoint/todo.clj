(ns donut.todo-example.backend.endpoint.todo
  (:require [honey.sql :as sql]
            [next.jdbc.sql :as jsql]))

(def parameters
  {:path [:map [:todo/id int?]]})

(defn todo-by-id
  [db params]
  (->> {:select [:*]
        :from   [:todo]
        :where  [:= :todo/id (:todo/id params)]}
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
                                 (dissoc all-params :todo/id)
                                 (select-keys all-params [:todo/id]))
                   {:status 200
                    :body   (todo-by-id db all-params)})}

    :delete
    {:parameters parameters
     :handler    (fn [{:keys [all-params db]}]
                   (jsql/delete! db :todo (select-keys all-params [:todo/id])))}}})
