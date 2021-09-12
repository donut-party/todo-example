(ns donut.todo-example.cross.endpoint-routes
  (:require [donut.system :as ds]
            [reitit.core :as r]))

(def routes
  (serr/expand-routes
   [{:ctx               {:db (ds/ref :sweet-tooth.endpoint.datomic/connection)}
     :id-key            :db/id
     :auth-id-key       :db/id
     ::serr/path-prefix "/api/v1"}
    [:donut.todo-example.backend.endpoint.todo-list]
    [:donut.todo-example.backend.endpoint.todo]]))
