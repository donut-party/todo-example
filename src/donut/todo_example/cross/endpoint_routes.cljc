(ns donut.todo-example.cross.endpoint-routes
  (:require [donut.endpoint.routes :as der]
            #?@(:clj
                [[donut.todo-example.backend.endpoint.todo]
                 [donut.todo-example.backend.endpoint.todo-list]])))

(def routes
  (-> [{:auth-id-key :id}
       [:donut.todo-example.backend.endpoint.todo-list
        {:id-key           :todo_list/id
         ::der/path-prefix "/api/v1"}]
       [:donut.todo-example.backend.endpoint.todo
        {:id-key           :todo/id
         ::der/expand-with [[:collection {::der/path-prefix "/api/v1/todo-list/{todo_list/id}"}]
                            [:member     {::der/path-prefix "/api/v1/todo-list/{todo/todo_list_id}"}]]}]]
      der/expand-routes
      #?(:clj der/merge-handlers)))
