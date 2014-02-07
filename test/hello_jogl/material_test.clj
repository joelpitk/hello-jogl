(ns hello-jogl.material-test
  (:refer-clojure :exclude [use])
  (:require [midje.sweet :refer :all]
            [hello-jogl.material :refer :all]
            [hello-jogl.shader-program :as shader-program]))

;TODO Refactor tests. Ugly mock hell...
;TODO Refactor use into three smaller functions?
(facts "about use"
  (prerequisites
    (add-to-programs! anything anything) => irrelevant
    (program-exists? anything anything) => irrelevant
    (program-of anything anything) => irrelevant
    (shader-program/use anything anything) => irrelevant)
  (fact "creates shader program when no program exists for given material"
    (use ..gl-context.. ..material..) => irrelevant
      (provided
        (get-programs) => ..programs..
        (program-exists? ..programs.. ..material..) => false
        (create-program ..gl-context.. ..material..) => irrelevant))

  (fact "does not create shader program when program exists for given material"
    (use ..gl-context.. ..material..) => irrelevant
      (provided
        (get-programs) => ..programs..
        (program-exists? ..programs.. ..material..) => true
        (create-program ..gl-context.. ..material..) => irrelevant :times 0))

  (fact "adds created shader program to programs"
    (use ..gl-context.. ..material..) => irrelevant
      (provided
        (get-programs) => ..programs..
        (program-exists? ..programs.. ..material..) => false
        (create-program ..gl-context.. ..material..) => ..created-program..
        (add-to-programs! ..material.. ..created-program..) => ..programs..))

  (fact "does not add created shader program to programs when program exists for given material"
    (use ..gl-context.. ..material..) => irrelevant
      (provided
        (get-programs) => ..programs..
        (program-exists? ..programs.. ..material..) => true
        (add-to-programs! ..material.. ..created-program..) => ..programs.. :times 0))

  (fact "uses program of material"
    (use ..gl-context.. ..material..) => irrelevant
      (provided
        (get-programs) => ..programs..
        (program-of ..programs.. ..material..) => ..program..
        (shader-program/use ..gl-context.. ..program..) => irrelevant)))

(facts "about program-exists?"
  (fact "returns true when material with same name exists in programs"
    (let [programs {"foo" ..material-program..
                    "bar" ..material-program..}
          material {:name "foo"}]
    (program-exists? programs material) => true))

  (fact "returns false when material with same name does not exist in programs"
    (let [programs {"foo" ..material-program..
                    "bar" ..material-program..}
          material {:name "non-existing"}]
      (program-exists? programs material) => false)))

(facts "about create-program"
  (fact "creates program for given material"
    (let [material {:vertex-shader-source ..vs-source..
                    :fragment-shader-source ..fs-source..}]
      (create-program ..gl-context.. material) => ..program..
        (provided (shader-program/create ..gl-context.. ..vs-source.. ..fs-source..) => ..program..))))

(facts "about program-of"
  (fact "return program of given material from programs by name"
    (let [programs {"foo" {:material ..foo-material.. :program ..foo-program..}
                    "bar" {:material ..bar-material.. :program ..bar-program..}}]
      (program-of programs {:name "foo"}) => ..foo-program..)))
