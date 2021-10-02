(ns donut.todo-example.backend.handler
  (:require [donut.endpoint.middleware :as dm]
            [donut.system :as ds]
            [donut.todo-example.cross.endpoint-routes :as der]
            [reitit.ring :as rr]))

(def router
  (-> der/routes
      rr/router
      rr/ring-handler))

(defn wrap-db
  [handler db]
  (fn [req]
    (handler (assoc req :db db))))

(defn handler
  [opts]
  (-> router
      (wrap-db (:db opts))
      dm/middleware))

(def HandlerComponent
  {:start (fn [config _ _] (handler config))
   :db    (ds/ref :db)})
