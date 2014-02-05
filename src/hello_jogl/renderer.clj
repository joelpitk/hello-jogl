(ns hello-jogl.renderer
  (:require [midje.sweet :refer [unfinished]]
            [hello-jogl.entity :as entity]
            [hello-jogl.vertex-array :as vertex-array]))

(def vertex-arrays {})

(defn add-to-vertex-arrays! [entity vertex-array]
  (def vertex-arrays (assoc vertex-arrays entity vertex-array)))

(defn vertex-array-exists-for [arrays entity]
  (contains? arrays entity))

(defn create-vertex-array-for [gl entity]
  (vertex-array/create gl (:geometry entity)))

(defn vertex-array-of [arrays entity] (get arrays entity))

(defn render [gl entity]
  (if (not (vertex-array-exists-for vertex-arrays entity))
    (add-to-vertex-arrays! entity (create-vertex-array-for gl entity)))
  (vertex-array/draw gl (vertex-array-of vertex-arrays entity)))

(defn renderable? [entity]
  (entity/has-components? entity :position :geometry))

(defn renderable [entities] nil
  (filter renderable? entities))

(defn render-all [gl entities]
  (doseq [renderable-entity (renderable entities)]
    (render gl renderable-entity)))
