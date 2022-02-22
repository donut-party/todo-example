(ns donut.todo-example.frontend.subs
  (:require
   [donut.frontend.core.utils :as dcu]
   [donut.frontend.nav.utils :as dnu]
   [re-frame.core :as rf]))

(rf/reg-sub :todo-lists
  (fn [db]
    (dcu/entities db :todo-list :todo_list/id)))

(rf/reg-sub :routed-todo-list
  (fn [db]
    (dnu/routed-entity db :todo-list :todo_list/id)))
