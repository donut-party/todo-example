(ns donut.todo-example.backend.endpoint.todo-list
  (:require [donut.todo-example.backend.query.todo-list :as qtl]
            [next.jdbc.sql :as jsql]))

(def parameters
  {:path [:map [:todo_list/id int?]]})

(def handlers
  {:collection
   {:get  (fn [{:keys [db]}]
            {:status 200
             :body   (qtl/todo-lists db)})
    :post (fn [{:keys [all-params db]}]
            {:status 200
             :body   (jsql/insert! db :todo_list all-params)})}

   :member
   {:get {:parameters parameters
          :handler    (fn [{:keys [all-params db]}]
                        {:status 200
                         :body   (qtl/todo-list-by-id db all-params)})}

    :put {:parameters parameters
          :handler    (fn [{:keys [all-params db]}]
                        (jsql/update! db
                                      :todo_list
                                      (dissoc all-params :todo_list/id)
                                      (select-keys all-params [:todo_list/id]))
                        {:status 200
                         :body   (qtl/todo-list-by-id db all-params)})}

    :delete {:parameters parameters
             :handler    (fn [{:keys [all-params db]}]
                           (jsql/delete! db :todo_list (select-keys all-params [:todo_list/id])))}}})
