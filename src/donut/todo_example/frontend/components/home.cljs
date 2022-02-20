(ns donut.todo-example.frontend.components.home
  (:require
   [donut.frontend.core.utils :as dcu]
   [donut.frontend.form.components :as dfc]
   [donut.frontend.nav.components :as dnc]
   [donut.todo-example.frontend.ui :as ui]
   [re-frame.core :as rf]))

(defn todo-list-li
  [todo-list]
  ;; TODO add key
  [ui/link
   (dnc/simple-route-link
    {:route-name   :todo-list
     :route-params todo-list}
    (:todo_list/title todo-list))])

(defn component
  []
  [:div
   [:h1 "Home"]
   (dfc/with-form [:post :todo-lists]
     [:form {:on-submit (dcu/prevent-default #(*submit))}
      [*field :text :title]])
   (let [todo-lists @(rf/subscribe [:todo-lists])]
     [ui/ul (map todo-list-li todo-lists)])])
