(ns hello-jogl.core-test
  (:require [midje.sweet :refer :all]
            [hello-jogl.core :refer :all]
            [hello-jogl.renderer :as renderer]))

(facts "about draw" :core
  (fact "renders all renderable entities"
    (draw ..gl-context.. ..all-entities..) => irrelevant
      (provided
        (renderer/renderable ..all-entities..) => ..renderable-entities..
        (renderer/render-all ..gl-context.. ..renderable-entities..) => irrelevant)))
