(ns donut.todo-example.frontend.components.todo-list
  (:require
   [re-frame.core :as rf]
   [donut.frontend.core.utils :as dcu]
   [donut.frontend.form.components :as dfc]
   [donut.frontend.form.flow :as dff]
   [donut.todo-example.frontend.ui :as ui]))

(defn show
  []
  (let [todo-list @(rf/subscribe [:routed-todo-list])
        todos     @(rf/subscribe [:routed-todos])]
    [:div
     [ui/h1 (:todo_list/title todo-list)]

     (dfc/with-form [:post :todos]
       [ui/form
        [:form {:on-submit
                (dcu/prevent-default #(*submit {:on           {:success [[::dff/clear-form :$ctx]]}
                                                :route-params todo-list}))}
         [(dcu/focus-component [*field :text :description {:placeholder           "add a todo"
                                                           :donut.field/no-label? true}])]]])

     (if (empty? todos)
       [:div.my-4 "no todos yet"]
       [ui/ul
        (mapv :todo/description todos)])]))
