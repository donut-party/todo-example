(ns dev
  {:clj-kondo/config {:linters {:unused-namespace {:level :off}}}}
  (:require
   [clojure.tools.namespace.repl :as nsrepl]
   [dev.hawk]
   [donut.endpoint.test.harness :as deth]
   [donut.endpoint.routes :as der]
   [donut.system :as ds]
   [donut.system.repl :as dsr]
   [donut.system.repl.state :as dsrs]
   [donut.todo-example.backend.system :as sys]
   [malli.core :as m]
   [migratus.core :as migratus]
   [muuntaja.core :as mu]
   [reitit.core :as r]
   [ring.mock.request :as ring-mock])
  (:refer-clojure :exclude [test]))

(nsrepl/set-refresh-dirs "dev/src" "src" "test")

(when-not dsrs/system
  (dsr/start))

(defn routes
  []
  (get-in dsrs/system [::ds/defs :http :routes]))

(def start dsr/start)
(def stop dsr/stop)

(defn db-config [] (get-in dsrs/system [::ds/instances :db :migratus]))
(defn router [] (get-in dsrs/system [::ds/instances :http :router]))
