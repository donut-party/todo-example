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
    (fn [{:keys [all-params dependencies]}]
      {:status 200
       :body   (qt/todos-by-todo-list-id (:datasource dependencies) all-params)})}

   :post
   {:parameters collection-parameters
    :handler
    (fn [{:keys [all-params dependencies]}]
      {:status 200
       :body   (jsql/insert! (:datasource dependencies)
                             :todo
                             (set/rename-keys all-params {:todo_list/id :todo_list_id}))})}})

(def member-parameters
  {:path [:map
          [:todo/id int?]
          [:todo/todo_list_id int?]]})

(def member-handlers
  {:get
   {:parameters member-parameters
    :handler    (fn [{:keys [all-params dependencies]}]
                  {:status 200
                   :body   (qt/todo-by-id (:datasource dependencies) all-params)})}

   :put
   {:parameters member-parameters
    :handler    (fn [{:keys [all-params dependencies]}]
                  (jsql/update! (:datasource dependencies)
                                :todo
                                (dissoc all-params :todo/id)
                                (select-keys all-params [:todo/id]))
                  {:status 200
                   :body   (qt/todo-by-id (:datasource dependencies) all-params)})}

   :delete
   {:parameters member-parameters
    :handler    (fn [{:keys [all-params dependencies]}]
                  (jsql/delete! (:datasource dependencies)
                                :todo
                                (select-keys all-params [:todo/id])))}})
