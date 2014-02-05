(ns hello-jogl.core-test
  (:require [midje.sweet :refer :all]
            [hello-jogl.core :refer :all]
            [hello-jogl.renderer :as renderer]))

(facts "about draw" :core
  (fact "renders all entities"
    (draw ..gl-context.. ..all-entities..) => irrelevant
      (provided
        (renderer/render-all ..gl-context.. ..all-entities..) => irrelevant)))
