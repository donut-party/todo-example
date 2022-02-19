(ns donut.todo-example.frontend.frontend-routes
  (:require
   [re-frame.core :as rf]
   [reitit.coercion.malli :as rm]))

(def routes
  [["/"
    {:name :home
     :components {:main [:div "hi"]}}]
   ["/todo-list/{id}"
    {:name :todo-list
     :components {:main [:div "todo-list"]}}]])
