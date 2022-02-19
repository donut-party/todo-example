(ns donut.todo-example.frontend.app
  (:require
   [re-frame.core :as rf]
   [donut.frontend.nav.components :as dnc]
   [donut.frontend.nav.flow :as dnf]))

(defn app
  []
  [:div
   [:div {:class "md:pl-64 flex flex-col flex-1"}
    [:main {:class "flex-1"}
     [:div {:class "max-w-7xl mx-auto py-6 px-4 sm:px-6 md:px-8"}
      (let [x @(rf/subscribe [::dnf/routed-component :main])]
        (prn "main" x)
        x)]]]])
