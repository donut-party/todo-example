(ns donut.todo-example.cross.endpoint-routes
  (:require [donut.sugar.routes :as dsr]
            #?@(:clj
                [[donut.todo-example.backend.endpoint.todo]
                 [donut.todo-example.backend.endpoint.todo-list]])))

(def routes
  (-> [{:id-key            :id
        :auth-id-key       :id
        ::dsr/path-prefix "/api/v1"}
       [:donut.todo-example.backend.endpoint.todo-list]
       [:donut.todo-example.backend.endpoint.todo]]
      dsr/expand-routes
      #?(:clj dsr/load-handlers!)))
