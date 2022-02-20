(ns donut.todo-example.frontend.ui
  (:require [lambdaisland.ornament :as o]))

(o/defstyled ul :div
  :max-w-7xl :mx-auto :sm:px-6 :lg:-px-8 :my-3
  {"--gi-divide-y-reverse" 0}
  [:>.list-container :bg-white :shadow :overflow-hidden :sm:rounded-md]
  [:>.list-container>ul :divide-y :divide-gray-200]
  [:a :block :p-3]
  ([list-items]
   [:<>
    (into
     [:div.list-container
      ;; todo what's the unwraping way to do this?
      (->> list-items
           (map (fn [li] [:li li]))
           (into [:ul {:role "list" :class "divide-y"}]))])]))

(o/defstyled link :span
  [:>a :text-blue-600 :hover:text-green-600 :hover:bg-gray-50]
  ([link]
   [:<> link]))
