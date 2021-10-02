(ns donut.todo-example.backend.endpoint.todo)

(def handlers
  {:collection
   {:get {:handler (fn [req]
                     {:status 200
                      :body "hi"})}}

   :member
   {:get {:handler (fn [req]
                     {:status 200
                      :body "hi"})}}})
