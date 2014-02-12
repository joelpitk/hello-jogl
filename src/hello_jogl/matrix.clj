(ns hello-jogl.matrix
  (:refer-clojure :exclude [* - + == /])
  (:require [clojure.algo.generic.math-functions :as math-functions])
  (:use clojure.core.matrix)
  (:use clojure.core.matrix.operators))

(core-matrix/set-current-implementation :vectorz)

(defn perspective [fov-y aspect-ratio near-plane far-plane]
  (let [y-scale (/ 1 (math/tan (to-radians (/ fov-y 2))))
        x-scale (/ y-scale aspect-ratio)
        frustrum-length (- far-plane near-plane)
        negated-division-of (fn [dividend divisor] (- (/ dividend divisor)))]
    (matrix [[x-scale 0 0 0]
             [0 y-scale 0 0]
             [0 0 (negated-division-of (+ near-plane far-plane) frustrum-length) (negated-division-of (* 2 near-plane far-plane) frustrum-length)]
             [0 0 -1 0]])))

