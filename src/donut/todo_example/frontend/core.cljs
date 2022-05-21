(ns donut.todo-example.frontend.core
  (:require [reagent.dom :as rdom]
            [re-frame.core :as rf]
            [donut.frontend.config :as dconf]
            [donut.frontend.core.flow :as dcf]
            [donut.frontend.core.utils :as dcu]
            [donut.frontend.nav.flow :as dnf]
            [donut.todo-example.cross.endpoint-routes :as endpoint-routes]
            [donut.todo-example.frontend.app :as app]
            [donut.todo-example.frontend.frontend-routes :as frontend-routes]
            [donut.todo-example.frontend.subs] ;; load subs
            [donut.system :as ds]
            [meta-merge.core :as meta-merge]))

(defn system-config
  "This is a function instead of a static value so that it will pick up
  reloaded changes"
  []
  (meta-merge/meta-merge
   dconf/default-config
   {::ds/defs
    {:donut.frontend
     {:sync-router     #::ds{:config {:routes endpoint-routes/routes}}
      :frontend-router #::ds{:config {:routes frontend-routes/routes}}}}}))

(defn ^:dev/after-load start []
  (rf/dispatch-sync [::dcf/start-system (system-config)])
  (rf/dispatch-sync [::dnf/dispatch-current])
  (rdom/render [app/app] (dcu/el-by-id "app")))

(defn init
  []
  (start))

(defn ^:dev/before-load stop [_]
  (rf/dispatch-sync [::dcf/stop-system]))
