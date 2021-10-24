(ns donut.todo-example.data
  (:require [clojure.spec.alpha :as s]
            [donut.endpoint.test.harness :as deth]
            [reifyhealth.specmonstah.core :as sm]
            [reifyhealth.specmonstah.spec-gen :as sg]
            [next.jdbc.sql :as jsql]))

(defn db
  []
  (deth/instance [:db :connection]))

(s/def :todo-list/title string?)

(s/def :todo-list/entity
  (s/keys :req-un [:todo-list/title]))


(s/def :todo/todo_list_id int?)
(s/def :todo/description string?)
(s/def :todo/done boolean?)

(s/def :todo/entity
  (s/keys :req-un [:todo/todo_list_id
                   :todo/description
                   :todo/done]))

(def specmonstah-schema
  {:todo      {:prefix    :t
               :spec      :todo/entity
               :relations {:todo_list_id [:todo-list :todo_list/id]}}
   :todo-list {:prefix :tl
               :spec   :todo-list/entity}})

(def table-names
  {:todo      :todo
   :todo-list :todo_list})

(defn copy-spec-gen-data
  [_ {:keys [spec-gen]}]
  spec-gen)

(defn perform-insert
  [ent-db {:keys [ent-type] :as opts}]
  (jsql/insert! (db)
                (ent-type table-names)
                (sg/spec-gen-assoc-relations ent-db opts)))

(def insert-generated
  [copy-spec-gen-data
   perform-insert])

(defn insert [query]
  (-> (sg/ent-db-spec-gen {:schema specmonstah-schema} query)
      (sm/visit-ents-once :inserted-data insert-generated)
      (sm/attr-map :inserted-data)))
