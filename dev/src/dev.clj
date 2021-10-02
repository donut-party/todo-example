(ns dev
  {:clj-kondo/config {:linters {:unused-namespace {:level :off}}}}
  (:require
   [clojure.tools.namespace.repl :as nsrepl]
   [dev.hawk]
   [donut.system :as ds]
   [donut.system.repl :as dsr]
   [donut.system.repl.state :as dsrs]
   [donut.todo-example.backend.system])
  (:refer-clojure :exclude [test]))

(nsrepl/set-refresh-dirs "dev/src" "src" "test")

(when-not dsrs/system
  (dsr/start))
