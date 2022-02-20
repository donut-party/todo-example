(ns donut.todo-example.backend.system
  (:require
   [donut.endpoint.middleware :as dm]
   [donut.system :as ds]
   [donut.todo-example.backend.handler :as dh]
   [donut.todo-example.cross.endpoint-routes :as der]
   [environ.core :as env]
   [migratus.core :as migratus]
   [muuntaja.core :as m]
   [next.jdbc :as jdbc]
   [reitit.coercion.malli :as rcm]
   [reitit.ring :as rr]
   [ring.adapter.jetty :as rj]))

(def config
  {::ds/defs
   {:env  {:http-port 3010}
    :http {:server     {:start (fn [{:keys [handler options]} _ _]
                                 (rj/run-jetty handler options))
                        :stop  (fn [_ instance _]
                                 (.stop instance))
                        :conf  {:handler (ds/ref :handler)
                                :options {:port  (ds/ref [:env :http-port])
                                          :join? false}}}
           :handler    {:start (fn [config _ _] (dh/handler config))
                        :conf  {:db         (ds/ref [:db :connection])
                                :router     (ds/ref :router)
                                :middleware (ds/ref :middleware)}}
           :middleware dm/AppMiddlewareComponent
           :routes     der/routes
           :router     {:start (fn [{:keys [routes router-opts]} _ _]
                                 (rr/router routes router-opts))
                        :conf  {:routes      (ds/ref :routes)
                                :router-opts {:data {:coercion   rcm/coercion
                                                     :muuntaja   m/instance
                                                     :middleware (ds/ref :route-middleware)}}}}
           :route-middleware {:start (fn [_ _ _] (dm/route-middleware))}}
    :db   {:connection {:start (fn [{:keys [uri]} _ _] (jdbc/get-datasource uri))
                        :conf  {:uri (env/env :db-uri "jdbc:postgresql://localhost/todoexample_dev?user=daniel&password=")}}
           :migratus   {:start (fn [{:keys [run?] :as opts} _ _]
                                 (when run? (migratus/migrate opts)))
                        :conf  {:run?          true
                                :db            (ds/ref :connection)
                                :store         :database
                                :migration-dir "migrations"}}}}})

(defmethod ds/config :dev
  [_]
  config)

(defmethod ds/config :test
  [_]
  (-> config
      (update-in [::ds/defs :http] dissoc :server)
      (ds/system-merge
       {::ds/defs {:db   {:connection {:conf {:uri "jdbc:postgresql://localhost/todoexample_test?user=daniel&password="}}
                          :migratus   {:conf {:run? false}}}
                   :http {:middleware {:conf {:security {:anti-forgery false}}}}}})))

(defmethod ds/config :donut.system/repl
  [_]
  (ds/config :dev))
