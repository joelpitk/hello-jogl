(ns hello-jogl.renderer
  (:require [midje.sweet :refer [unfinished]]
            [hello-jogl.entity :as entity]
            [hello-jogl.vertex-array :as vertex-array]))

(defn renderable? [entity]
  (entity/has-components? entity :geometry))

(defn renderable [entities]
  (filter renderable? entities))

(def entity-vertex-arrays (atom {}))

(defn get-entity-vertex-arrays [] @entity-vertex-arrays)

(defn add-to-vertex-arrays! [entity vertex-array]
  (reset! entity-vertex-arrays (assoc @entity-vertex-arrays entity vertex-array)))

(defn remove-from-vertex-arrays! [entity]
  (reset! entity-vertex-arrays (dissoc @entity-vertex-arrays entity)))

(defn vertex-array-exists-for [vertex-arrays entity]
  (contains? vertex-arrays entity))

(defn create-vertex-array-for [gl entity]
  (vertex-array/create gl (:geometry entity)))

(defn vertex-array-of [vertex-arrays entity]
  (get vertex-arrays entity))

(defn vertex-arrays-of [vertex-arrays entities]
  (map vertex-arrays entities))

(defn entities-of [vertex-arrays]
  (keys vertex-arrays))

(defn orphaned-entities [vertex-arrays renderable-entities]
  (let [entities-with-vertex-arrays (set (entities-of vertex-arrays))
        renderable-entities (set renderable-entities)
        no-longer-renderable-entities (clojure.set/difference entities-with-vertex-arrays renderable-entities)]
  no-longer-renderable-entities))

(defn delete-orphaned-vertex-arrays [gl vertex-arrays renderable-entities]
  (let [orphan-entities (orphaned-entities vertex-arrays renderable-entities)
        orphan-vertex-arrays (vertex-arrays-of vertex-arrays orphan-entities)]
  (doseq [orphan-vertex-array orphan-vertex-arrays]
    (vertex-array/delete gl orphan-vertex-array))
  (doseq [orphan-entity orphan-entities]
    (remove-from-vertex-arrays! orphan-entity))))

(defn render [gl entity]
  (when-not (vertex-array-exists-for (get-entity-vertex-arrays) entity)
    (add-to-vertex-arrays! entity (create-vertex-array-for gl entity)))
  (vertex-array/draw gl (vertex-array-of (get-entity-vertex-arrays) entity)))

(defn render-all [gl entities]
  (let [renderable-entities (renderable entities)]
    (delete-orphaned-vertex-arrays gl (get-entity-vertex-arrays) renderable-entities)
    (doseq [renderable-entity renderable-entities]
      (render gl renderable-entity))))

(defn dispose [gl]
  (doseq [vertex-array (vals (get-entity-vertex-arrays))]
    (vertex-array/delete gl vertex-array)))
