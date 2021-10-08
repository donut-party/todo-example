(ns donut.todo-example.backend.system
  (:require [donut.system :as ds]
            [donut.todo-example.backend.handler :as dh]
            [ring.adapter.jetty :as rj]))

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
           :handler {:start (fn [config _ _] (dh/handler config))
                     :db    (ds/ref [:db :connection])}}
    :db   {:connection "postgresql://localhost/todoexample_dev?user=daniel&password="
           :migratus   {:start         (fn [opts _ _])
                        :db            (ds/ref :connection)
                        :store         :database
                        :migration-dir "migrations"}}}})

(defmethod ds/config :dev
  [_]
  config)

(defmethod ds/config :donut.system/repl
  [_]
  (ds/config :dev))
