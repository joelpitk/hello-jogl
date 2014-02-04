(ns hello-jogl.renderer-test
  (:require [midje.sweet :refer :all]
            [hello-jogl.renderer :refer :all]
            [hello-jogl.entity :as entity]))

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
