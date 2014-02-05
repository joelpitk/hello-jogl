(ns hello-jogl.renderer-test
  (:require [midje.sweet :refer :all]
            [hello-jogl.renderer :refer :all]
            [hello-jogl.entity :as entity]
            [hello-jogl.vertex-array :as vertex-array]))

(facts "about renderable"
  (fact "returns only renderable entities"
    (let [entities [..renderable.. ..non-renderable.. ..another-renderable..]]
      (renderable entities) => [..renderable.. ..another-renderable..]
        (provided
          (renderable? ..renderable..) => true
          (renderable? ..non-renderable..) => false
          (renderable? ..another-renderable..) => true))))

(facts "about renderable?"
  (fact "entity with geometry and position is renderable"
    (renderable? ..entity..) => true
      (provided
        (entity/has-components? ..entity.. :position :geometry) => true))

  (fact "entity without geometry and position is not renderable"
    (renderable? ..entity..) => false
      (provided
        (entity/has-components? ..entity.. :position :geometry) => false)))

(facts "about render-all"
  (fact "renders all renderable entities"
    (render-all ..gl-context.. ..entities..) => irrelevant
      (provided
        (renderable ..entities..) => [..renderable.. ..another-renderable..]
        (render ..gl-context.. ..renderable..) => irrelevant
        (render ..gl-context.. ..another-renderable..) => irrelevant)))

(facts "about render"
  (prerequisite (vertex-array/draw ..gl-context.. anything) => irrelevant)
  (fact "creates vertex array for entity when vertex array for entity does not exist"
    (render ..gl-context.. ..entity..) => irrelevant
      (provided
        (vertex-array-exists-for vertex-arrays ..entity..) => false
        (create-vertex-array-for ..gl-context.. ..entity..) => irrelevant))

  (fact "adds created vertex-array to vertex-arrays when vertex array for entity does not exist"
    (render ..gl-context.. ..entity..) => irrelevant
      (provided
        (vertex-array-exists-for vertex-arrays ..entity..) => false
        (create-vertex-array-for ..gl-context.. ..entity..) => ..created-vertex-array..
        (add-to-vertex-arrays! ..entity.. ..created-vertex-array..) => vertex-arrays))

  (fact "does not create vertex array for entity when vertex array for entity exists"
    (render ..gl-context.. ..entity..) => irrelevant
      (provided
        (vertex-array-exists-for vertex-arrays ..entity..) => true
        (create-vertex-array-for ..gl-context.. ..entity..) => irrelevant :times 0))

  (fact "does not add created vertex-array to vertex-arrays when vertex array for entity does exists"
    (render ..gl-context.. ..entity..) => irrelevant
      (provided
        (vertex-array-exists-for vertex-arrays ..entity..) => true
        (add-to-vertex-arrays! ..entity.. ..created-vertex-array..) => irrelevant :times 0))

  (fact "draws vertex array of entity"
    (render ..gl-context.. ..entity..) => irrelevant
      (provided
        (vertex-array-exists-for vertex-arrays ..entity..) => irrelevant
        (vertex-array-of vertex-arrays ..entity..) => ..vertex-array-of-entity..
        (vertex-array/draw ..gl-context.. ..vertex-array-of-entity..) => irrelevant)))

(facts "about vertex-array-exists-for"
  (fact "returns true when vertex array with entity as key exists in given vertex arrays"
    (vertex-array-exists-for {..entity.. ..vertex-array..} ..entity..) => true)

  (fact "returns false when vertex array with entity as key does not exist in given vertex arrays"
    (vertex-array-exists-for {..entity.. ..vertex-array..} ..another-entity..) => false))

(facts "about create-vertex-array-for"
  (fact "returns vertex array created from geometry of entity"
    (let [entity {:geometry ..geometry..}]
      (create-vertex-array-for ..gl-context.. entity) => ..vertex-array-from-geometry..
        (provided (vertex-array/create ..gl-context.. ..geometry..) => ..vertex-array-from-geometry..))))

(facts "about vertex-array-of"
  (fact "returns vertex array of given entity"
    (let [arrays {..entity.. ..vertex-array.. ..another-entity.. ..another-vertex-array..}]
      (vertex-array-of arrays ..entity..) => ..vertex-array..)))
