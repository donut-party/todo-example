(ns donut.todo-example.backend.endpoint.todo)

(def handlers
  {:collection
   {:get {:handler (fn [req]
                     {:status  200
                      :headers {"Content-Type" "text/html"}
                      :body    "todo list"})}}

   :member
   {:get {:handler (fn [req]
                     {:status 200
                      :body   "todo"})}}})
