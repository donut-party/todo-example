(ns donut.todo-example.frontend.components.todo-list
  (:require
   [reagent.core :as r]
   [re-frame.core :as rf]
   [donut.frontend.core.utils :as dcu]
   [donut.frontend.form.components :as dfc]
   [donut.frontend.form.flow :as dff]
   [donut.todo-example.frontend.ui :as ui]
   [lambdaisland.ornament :as o]))

(o/defstyled todo-text :div
  :p-3 :hover:bg-gray-50 :cursor-pointer
  ([{:keys [on-click]} text]
   ^{:on-click on-click}
   [:<> text]))

(o/defstyled todo-input :div
  [:input :p-3 :w-full :focus:bg-yellow-50 :focus:outline-none])

(o/defstyled delete-btn :div
  :float-right :bg-red-500 :text-sm :text-white :px-2 :py-1
  :rounded-md :hover:bg-red-700 :hover:cursor-pointer
  ([{:keys [on-click]} text]
   ^{:on-click on-click}
   [:<> text]))

(defn todo-item
  [_todo]
  (let [editing? (r/atom false)]
    (fn [todo]
      (dfc/with-form [:put :todo (select-keys todo [:todo/id :todo/todo_list_id])]
        [:div {:class "flex flex-row"}
         [:div {:class "grow"}
          (if @editing?

            [todo-input
             [(dcu/focus-component
               [*input :text :todo/description
                {:donut.input/on-blur (fn [_]
                                        (*submit)
                                        (swap! editing? not))}])]]
            [todo-text
             {:on-click (fn [_]
                          (rf/dispatch [::dff/set-form *form-layout {:buffer todo}])
                          (swap! editing? not))}
             (:todo/description todo)])]
         [:div {:class "basis-2 pt-3 pr-3"}
          [delete-btn
           {:on-click #(rf/dispatch [:delete-todo todo])}
           "delete"]]]))))

(defn show
  []
  (let [todo-list @(rf/subscribe [:routed-todo-list])
        todos     @(rf/subscribe [:routed-todos])]
    [:div
     [delete-btn
      {:on-click #(rf/dispatch [:delete-todo-list todo-list])}
      "delete"]
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
        (mapv (fn [t] [todo-item t])
              todos)])]))
