(ns donut.todo-example.cross.endpoint-routes
  (:require [donut.sugar.routes :as dsr]))

(def routes
  (-> [{:id-key            :db/id
        :auth-id-key       :db/id
        ::dsr/path-prefix "/api/v1"}
       [:donut.todo-example.backend.endpoint.todo-list]
       [:donut.todo-example.backend.endpoint.todo]]
      dsr/expand-routes
      #?(:clj dsr/load-handlers!)))
