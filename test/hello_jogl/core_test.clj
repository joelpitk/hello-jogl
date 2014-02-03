(ns hello-jogl.core-test
  (:use midje.sweet)
  (:require [clojure.test :refer :all]
            [hello-jogl.core :refer :all]))

(facts "about has-component?"
 (let [entity {:component ..some-component..}]
   (fact "returns true when entity has given component"
    (has-component? entity :component) => true)

   (fact "returns false when entity does not have given component"
    (has-component? entity :non-existing) => false)

   (fact "returns false when entity does not have any components"
    (has-component? {} :component) => false)))

(facts "about has-components?"
  (let [entity {:foo ..foo-component.. :bar ..bar-component..}]
    (fact "returns true when entity has all given components"
      (has-components? entity :foo :bar) => true)

    (fact "returns false when entity does not have one of the given components"
      (has-components? entity :foo :baz :bar ) => false)

    (fact "returns false when entity does not have any of the given components"
      (has-components? entity :non-existing) => false)

    (fact "checks every component"
      (has-components? entity :one :two :three) => irrelevant
        (provided
          (has-component? entity :one) => irrelevant :times 1
          (has-component? entity :two) => irrelevant :times 1
          (has-component? entity :three) => irrelevant :times 1))))
