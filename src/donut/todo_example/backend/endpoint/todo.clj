(ns donut.todo-example.backend.endpoint.todo
  (:require
   [donut.todo-example.backend.query.todo :as qt]
   [next.jdbc.sql :as jsql]
   [clojure.set :as set]))

(def collection-parameters
  {:path [:map [:todo_list/id int?]]})

(def collection-handlers
  {:get
   {:parameters collection-parameters
    :handler
    (fn [{:keys [db all-params]}]
      {:status 200
       :body   (qt/todos-by-todo-list-id db all-params)})}

   :post
   {:parameters collection-parameters
    :handler
    (fn [{:keys [all-params db]}]
      {:status 200
       :body   (jsql/insert! db :todo (set/rename-keys all-params {:todo_list/id :todo_list_id}))})}})

(def member-parameters
  {:path [:map
          [:todo/id int?]
          [:todo/todo_list_id int?]]})

(def member-handlers
  {:get
   {:parameters member-parameters
    :handler    (fn [{:keys [all-params db]}]
                  {:status 200
                   :body   (qt/todo-by-id db all-params)})}

   :put
   {:parameters member-parameters
    :handler    (fn [{:keys [all-params db]}]
                  (jsql/update! db
                                :todo
                                (dissoc all-params :todo/id)
                                (select-keys all-params [:todo/id]))
                  {:status 200
                   :body   (qt/todo-by-id db all-params)})}

   :delete
   {:parameters member-parameters
    :handler    (fn [{:keys [all-params db]}]
                  (jsql/delete! db :todo (select-keys all-params [:todo/id])))}})
