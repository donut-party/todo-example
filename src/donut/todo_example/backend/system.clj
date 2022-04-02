(ns donut.todo-example.backend.system
  (:require
   [aero.core :as aero]
   [clojure.java.io :as io]
   [donut.middleware :as dm]
   [donut.system :as ds]
   [donut.todo-example.backend.handler :as dh]
   [donut.todo-example.cross.endpoint-routes :as der]
   [environ.core :as env]
   [migratus.core :as migratus]
   [next.jdbc :as jdbc]
   [ring.adapter.jetty :as rj]))

(defn env-config [& [profile]]
  (aero/read-config (io/resource "config/env.edn")
                    (when profile {:profile profile})))

(def base-system
  {::ds/defs
   {:env
    (env-config)

    :middleware
    (assoc dm/MiddlewareComponentGroup :routes der/routes)

    :http
    {:server
     {:start (fn [{:keys [handler options]} _ _]
               (rj/run-jetty handler options))
      :stop  (fn [_ instance _]
               (.stop instance))
      :conf  {:handler (ds/ref :handler)
              :options {:port  (ds/ref [:env :http-port])
                        :join? false}}}

     :handler
     {:start (fn [conf _ _] (dh/handler conf))
      :conf  {:db         (ds/ref [:db :connection])
              :router     (ds/ref [:middleware :router])
              :middleware (ds/ref [:middleware :middleware])}}}

    :db
    {:connection
     {:start (fn [{:keys [uri]} _ _] (jdbc/get-datasource uri))
      :conf  {:uri (env/env :db-uri "jdbc:postgresql://localhost/todoexample_dev?user=daniel&password=")}}

     :migratus
     {:start (fn [{:keys [run?] :as opts} _ _]
               (when run? (migratus/migrate opts)))
      :conf  {:run?          true
              :db            (ds/ref :connection)
              :store         :database
              :migration-dir "migrations"}}}}})

(defmethod ds/named-system :base
  [_]
  base-system)

(defmethod ds/named-system :dev
  [_]
  (ds/system :base {[:env] (env-config :dev)}))

(defmethod ds/named-system :donut.system/repl
  [_]
  (ds/system :dev))

(defmethod ds/named-system :test
  [_]
  (ds/system :dev
    {[:db :connection :conf :uri] "jdbc:postgresql://localhost/todoexample_test?user=daniel&password="
     [:db :migratus :conf :run?]  false
     [:http :server]              ::disabled

     [:http :middleware :conf :security :anti-forgery] false}))
