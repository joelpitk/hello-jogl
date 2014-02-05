(ns hello-jogl.renderer
  (:require [midje.sweet :refer [unfinished]]
            [hello-jogl.entity :as entity]
            [hello-jogl.vertex-array :as vertex-array]))

(def vertex-arrays-atom (atom {}))

(defn get-vertex-arrays [] @vertex-arrays-atom)

(defn add-to-vertex-arrays! [entity vertex-array]
  (reset! vertex-arrays-atom (assoc @vertex-arrays-atom entity vertex-array)))

(defn vertex-array-exists-for [arrays entity]
  (contains? arrays entity))

(defn create-vertex-array-for [gl entity]
  (vertex-array/create gl (:geometry entity)))

(defn vertex-array-of [arrays entity] (get arrays entity))

(defn render [gl entity]
  (if (not (vertex-array-exists-for (get-vertex-arrays) entity))
    (add-to-vertex-arrays! entity (create-vertex-array-for gl entity)))
  (vertex-array/draw gl (vertex-array-of (get-vertex-arrays) entity)))

(defn renderable? [entity]
  (entity/has-components? entity :geometry))

(defn renderable [entities] nil
  (filter renderable? entities))

(defn render-all [gl entities]
  (doseq [renderable-entity (renderable entities)]
    (render gl renderable-entity)))

(defn dispose [gl]
  (doseq [vertex-array (vals (get-vertex-arrays))]
    (vertex-array/delete gl vertex-array)))
