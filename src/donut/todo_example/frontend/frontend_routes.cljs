(ns donut.todo-example.frontend.frontend-routes
  (:require
   [re-frame.core :as rf]
   [reitit.coercion.malli :as rm]
   [donut.frontend.sync.flow :as dsf]
   [donut.todo-example.frontend.components.home :as h]
   [donut.todo-example.frontend.components.todo-list.list :as tll]))

(def routes
  [["/"
    {:name       :home
     :components {:main [h/component]}
     :lifecycle  {:enter [[::dsf/get :todo-lists]]}}]
   ["/todo-list"
    {:name       :todo-lists
     :components {:main [tll/component]}
     :lifecycle  {:enter [[::dsf/get :todo-lists]]}}]
   ["/todo-list/{id}"
    {:name       :todo-list
     :components {:main [:div "todo-list"]}}]])
