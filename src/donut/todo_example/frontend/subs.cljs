(ns donut.todo-example.frontend.subs
  (:require
   [donut.frontend.core.utils :as dcu]
   [re-frame.core :as rf]))

(rf/reg-sub :todo-lists
  (fn [db]
    (dcu/entities db :todo-list :id)))
