(ns donut.todo-example.frontend.components.home
  (:require
   [donut.frontend.core.utils :as dcu]
   [donut.frontend.form.components :as dfc]
   [donut.frontend.form.flow :as dff]
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
   [:div {:class "mb-8"}
    [ui/h2 "New Todo List"]
    (dfc/with-form [:post :todo-lists]
      [ui/form
       [:form {:on-submit (dcu/prevent-default #(*submit {:on {:success [[::dff/clear-form :$ctx]]}}))}
        [*field :text :title {:placeholder "todo list title"}]]])]
   [:div
    [ui/h2 "Todo Lists"]
    (let [todo-lists @(rf/subscribe [:todo-lists])]
      [ui/ul (map todo-list-li todo-lists)])]])
