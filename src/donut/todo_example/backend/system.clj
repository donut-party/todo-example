(ns donut.todo-example.backend.system
  (:require [donut.system :as ds]
            [donut.todo-example.backend.handler :as dh]
            [next.jdbc :as jdbc]
            [ring.adapter.jetty :as rj]
            [migratus.core :as migratus]))

(def config
  {::ds/defs
   {:env  {:http-port 8080}
    :http {:server  {:start   (fn [{:keys [handler options]} _ _]
                                (rj/run-jetty handler options))
                     :stop    (fn [_ instance _]
                                (.stop instance))
                     :handler (ds/ref :handler)
                     :options {:port  (ds/ref [:env :http-port])
                               :join? false}}
           :handler {:start  (fn [config _ _] (dh/handler config))
                     :db     (ds/ref [:db :connection])
                     :router (ds/ref :router)}
           :router  dh/router}
    :db   {:uri        "jdbc:postgresql://localhost/todoexample_dev?user=daniel&password="
           :connection {:start (fn [{:keys [uri]} _ _] (jdbc/get-datasource uri))
                        :uri   (ds/ref :uri)}
           :migratus   {:start         (fn [opts _ _] opts)
                        :after-start   (fn [opts _ _]
                                         (migratus/migrate opts))
                        :db            (ds/ref :connection)
                        :store         :database
                        :migration-dir "migrations"}}}})

(defmethod ds/config :dev
  [_]
  config)

(defmethod ds/config :test
  [_]
  (-> config
      (assoc-in [::ds/defs :http :server] nil)
      (assoc-in [::ds/defs :db :uri] "jdbc:postgresql://localhost/todoexample_test?user=daniel&password=")))

(defmethod ds/config :donut.system/repl
  [_]
  (ds/config :dev))
