(ns donut.todo-example.frontend.handlers
  (:require
   [donut.frontend.core.utils :as dcu]
   [donut.frontend.nav.flow :as dnf]
   [donut.frontend.sync.flow :as dsf]
   [re-frame.core :as rf]))

(rf/reg-event-fx :delete-todo-list
  [rf/trim-v]
  (fn [{:keys [db]} [todo-list]]
    {:db (dcu/dissoc-entity db :todo-list (:todo_list/id todo-list))
     :fx [[:dispatch [::dsf/delete :todo-list {:route-params todo-list
                                               :on {:success [::dnf/navigate-route :home]}}]]]}))
