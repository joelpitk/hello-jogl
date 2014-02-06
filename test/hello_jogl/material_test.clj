(ns hello-jogl.material-test
  (:refer-clojure :exclude [use])
  (:require [midje.sweet :refer :all]
            [hello-jogl.material :refer :all]
            [hello-jogl.shader-program :as shader-program]))

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
  (fact "returns true when material exists in programs"
    (program-exists? {..material.. ..program..} ..material..) => true)

  (fact "returns false when material does not exist in programs"
    (program-exists? {..material.. ..program..} ..non-existing-material..) => false))

(facts "about create-program"
  (fact "TODO"
    true => true))
