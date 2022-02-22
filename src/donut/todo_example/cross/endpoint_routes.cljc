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
         ;; this is experimental
         ;;
         ;; the rationale here is that you're likely to pass in a todo list to
         ;; generate the collection path, and a todo for the member routes
         ;;
         ;; without this differentiation you'd have to create param helpers to
         ;; map records to params. which might end up being the right path but
         ;; it's annoying
         ::der/expand-with [[:collection {::der/path-prefix "/api/v1/todo-list/{todo_list/id}"}]
                            [:member     {::der/path-prefix "/api/v1/todo-list/{todo/todo_list_id}"}]]}]]
      der/expand-routes
      #?(:clj der/merge-handlers)))
