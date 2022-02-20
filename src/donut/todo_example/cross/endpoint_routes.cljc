(ns donut.todo-example.cross.endpoint-routes
  (:require [donut.endpoint.routes :as der]
            #?@(:clj
                [[donut.todo-example.backend.endpoint.todo]
                 [donut.todo-example.backend.endpoint.todo-list]])))

(def routes
  (-> [{:id-key           :todo_list/id
        :auth-id-key      :id
        ::der/path-prefix "/api/v1"}
       [:donut.todo-example.backend.endpoint.todo-list]
       [:donut.todo-example.backend.endpoint.todo]]
      der/expand-routes
      #?(:clj der/merge-handlers)))
