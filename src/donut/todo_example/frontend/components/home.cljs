(ns donut.todo-example.frontend.components.home
  (:require
   [donut.frontend.core.utils :as dcu]
   [donut.frontend.form.components :as dfc]))

(defn component
  []
  [:div
   [:h1 "Home"]
   (dfc/with-form [:post :todo-lists]
     [:form {:on-submit (dcu/prevent-default #(*submit))}
      [*field :text :title]])])
