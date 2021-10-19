(ns donut.todo-example.backend.handler
  (:require [donut.endpoint.middleware :as dm]
            [donut.todo-example.cross.endpoint-routes :as der]
            [muuntaja.core :as m]
            [reitit.coercion.malli :as rcm]
            [reitit.ring :as rr]))

(def router
  (-> der/routes
      (rr/router {:data {:coercion   rcm/coercion
                         :muuntaja   m/instance
                         :middleware dm/route-middleware}})
      rr/ring-handler))

(defn wrap-db
  [handler db]
  (fn [req]
    (handler (assoc req :db db))))

(defn handler
  [{:keys [router db]}]
  (-> router
      (wrap-db db)
      dm/app-middleware))
