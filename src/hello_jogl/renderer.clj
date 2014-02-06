(ns hello-jogl.renderer
  (:require [midje.sweet :refer [unfinished]]
            [hello-jogl.entity :as entity]
            [hello-jogl.vertex-array :as vertex-array]))

;TODO: Rename vertex-arrays to something more descriptive
; as it is actually a map of entity vertex-array pairs

(def vertex-arrays-atom (atom {}))

(defn get-vertex-arrays [] @vertex-arrays-atom)

(defn add-to-vertex-arrays! [entity vertex-array]
  (reset! vertex-arrays-atom (assoc @vertex-arrays-atom entity vertex-array)))

(defn remove-from-vertex-arrays! [entity]
  (reset! vertex-arrays-atom (dissoc @vertex-arrays-atom entity)))

(defn vertex-array-exists-for [arrays entity]
  (contains? arrays entity))

(defn create-vertex-array-for [gl entity]
  (vertex-array/create gl (:geometry entity)))

(defn vertex-array-of [arrays entity] (get arrays entity))

(defn vertex-arrays-of [arrays entities]
  (map arrays entities))

(defn orphaned-entities [arrays renderable-entities]
  (let [entities-with-vertex-arrays (set (keys arrays))
        renderable-entities (set renderable-entities)
        no-longer-renderable-entities (clojure.set/difference entities-with-vertex-arrays (set renderable-entities))]
  no-longer-renderable-entities))

(defn delete-orphaned-vertex-arrays [gl arrays renderable-entities]
  (let [orphan-entities (orphaned-entities arrays renderable-entities)
        orphan-vertex-arrays (vertex-arrays-of (get-vertex-arrays) orphan-entities)]
  (doseq [orphan-vertex-array orphan-vertex-arrays]
    (vertex-array/delete gl orphan-vertex-array))
  (doseq [orphan-entity orphan-entities]
    (remove-from-vertex-arrays! orphan-entity))))

(defn render [gl entity]
  (if (not (vertex-array-exists-for (get-vertex-arrays) entity))
    (add-to-vertex-arrays! entity (create-vertex-array-for gl entity)))
  (vertex-array/draw gl (vertex-array-of (get-vertex-arrays) entity)))

(defn renderable? [entity]
  (entity/has-components? entity :geometry))

(defn renderable [entities] nil
  (filter renderable? entities))

(defn render-all [gl entities]
  (let [renderable-entities (renderable entities)]
    (delete-orphaned-vertex-arrays gl (get-vertex-arrays) renderable-entities)
    (doseq [renderable-entity renderable-entities]
      (render gl renderable-entity))))

(defn dispose [gl]
  (doseq [vertex-array (vals (get-vertex-arrays))]
    (vertex-array/delete gl vertex-array)))
