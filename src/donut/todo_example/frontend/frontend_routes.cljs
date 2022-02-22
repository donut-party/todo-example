(ns donut.todo-example.frontend.frontend-routes
  (:require
   [reitit.coercion.malli :as rm]
   [donut.frontend.sync.flow :as dsf]
   [donut.todo-example.frontend.components.home :as h]
   [donut.todo-example.frontend.components.todo-list :as tl]))

(def routes
  [["/"
    {:name       :home
     :components {:main [h/component]}
     :lifecycle  {:enter [[::dsf/get :todo-lists]]}}]
   ["/todo-list/{todo_list/id}"
    {:name       :todo-list
     :components {:main [tl/show]}
     :coercion   rm/coercion
     :lifecycle  {:enter [[::dsf/get :todo-list {:rules #{:merge-route-params}}]]}
     :parameters {:path [:map [:todo_list/id int?]]}}]])
