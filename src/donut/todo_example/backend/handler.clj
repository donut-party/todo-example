(ns donut.todo-example.backend.handler
  (:require [donut.endpoint.middleware :as dm]
            [donut.todo-example.backend.endpoint.todo :as todo]
            [donut.todo-example.backend.endpoint.todo :as todo-list]
            [reitit.core :as r]))

(def router
  (r/router [["/api/v1/"]]))

(defn handler
  []
  (dm/middleware router))
