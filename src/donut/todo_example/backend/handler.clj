(ns donut.todo-example.backend.handler
  (:require [donut.endpoint.middleware :as dm]
            [donut.system :as ds]
            [donut.todo-example.cross.endpoint-routes :as der]
            [reitit.coercion.malli :as rcm]
            [reitit.ring :as rr]
            [reitit.ring.coercion :as rrc]
            [reitit.ring.middleware.parameters :as rrmp]))

(def router
  (-> der/routes
      (rr/router {:data {:coercion rcm/coercion
                         :middleware [rrmp/parameters-middleware
                                      rrc/coerce-request-middleware
                                      rrc/coerce-response-middleware]}})
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
