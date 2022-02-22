(ns donut.todo-example.frontend.components.todo-list
  (:require
   [re-frame.core :as rf]
   [donut.todo-example.frontend.ui :as ui]))

(defn show
  []
  (let [todo-list @(rf/subscribe [:routed-todo-list])]
    [:div
     [ui/h1 (:todo_list/title todo-list)]
     [ui/ul
      [[:li "list item 1"]
       [:li "list item 2"]
       [:li "list item 3"]
       [:li "list item 4"]
       [:li "list item 5"]]]]))
