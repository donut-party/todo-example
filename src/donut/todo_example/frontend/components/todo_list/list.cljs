(ns donut.todo-example.frontend.components.todo-list.list
  (:require
   [donut.todo-example.frontend.ui :as ui]))

(defn component
  []
  [:div "my list"
   [ui/ul
    [:li "list item 1"]
    [:li "list item 2"]
    [:li "list item 3"]
    [:li "list item 4"]
    [:li "list item 5"]]])
