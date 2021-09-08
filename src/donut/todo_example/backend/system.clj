(ns donut.todo-example.backend.system
  (:require [donut.system :as ds]
            [ring.adapter.jetty :as rj]))

(def config
  {::ds/defs
   {:env {:http-port 8080}
    :http {:server  {:start   (fn [{:keys [handler options]} _ _]
                                (rj/run-jetty handler options))
                     :stop    (fn [_ instance _]
                                (.stop instance))
                     :handler (ds/ref :handler)
                     :options {:port  (ds/ref [:env :http-port])
                               :join? false}}
           :handler (fn [_req]
                      {:status  200
                       :headers {"ContentType" "text/html"}
                       :body    "It's donut.system, baby!"})}
    :db   {:crux nil}}})


(defmethod ds/config :dev
  [_]
  config)
