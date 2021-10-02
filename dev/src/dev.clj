(ns dev
  {:clj-kondo/config {:linters {:unused-namespace {:level :off}}}}
  (:require
   [clojure.tools.namespace.repl :as nsrepl]
   [donut.system :as ds]
   [donut.system.repl :as dsr]
   [donut.todo-example.backend.system]
   [hawk.core :as hawk])
  (:refer-clojure :exclude [test]))

(nsrepl/set-refresh-dirs "dev/src" "src" "test")

(defn auto-reset-handler [ctx _event]
  (binding [*ns* *ns*]
    (dsr/restart)
    ctx))

(defn- source-file? [_ {:keys [file]}]
  (re-find #"(\.cljc?|\.edn)$" (.getName file)))

(def hawk-watch!
  (delay (hawk/watch! [{:paths   ["src/" "resourcs/" "dev/src/" "dev/resoruces/"]
                        :filter  source-file?
                        :handler auto-reset-handler}])))
