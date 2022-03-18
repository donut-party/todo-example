(ns donut.todo-example.cross.endpoint-routes
  (:require [donut.routes :as dr]
            #?@(:clj
                [[donut.todo-example.backend.endpoint.todo :as todo]
                 [donut.todo-example.backend.endpoint.todo-list :as todo-list]])))

(def routes
  (dr/merge-route-opts
   [["/api/v1/todo-list"
     {:name     :todo-lists
      :ent-type :todo-list
      :id-key   :todo_list/id}
     #?(:clj todo-list/collection-handlers)]

    ["/api/v1/todo-list/{todo_list/id}"
     {:name     :todo-list
      :ent-type :todo-list
      :id-key   :todo_list/id}
     #?(:clj todo-list/member-handlers)]

    ["/api/v1/todo-list/{todo_list/id}/todo"
     {:name     :todos
      :ent-type :todo
      :id-key   :todo/id}
     #?(:clj todo/collection-handlers)]

    ["/api/v1/todo-list/{todo/todo_list_id}/todo/{todo/id}"
     {:name     :todo
      :ent-type :todo
      :id-key   :todo/id}
     #?(:clj todo/member-handlers)]]))
