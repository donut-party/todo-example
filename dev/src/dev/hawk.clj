(ns dev.hawk
  (:require [clojure.tools.namespace.repl :as repl]
            [donut.system.repl :as dsr]
            [hawk.core :as hawk]))

(repl/disable-reload!)

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
@hawk-watch!
