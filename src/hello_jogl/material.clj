(ns hello-jogl.material
  (:refer-clojure :exclude [use])
  (:require [midje.sweet :refer [unfinished]]
            [hello-jogl.shader-program :as shader-program]))

(def material-programs (atom {}))

(defn get-programs []
  @material-programs)

(defn add-to-programs! [material program]
  (reset! material-programs (assoc @material-programs (:name material) {:material material :program program})))

(defn program-exists? [programs material]
  (contains? programs (:name material)))

(defn create-program [gl material]
  (shader-program/create gl (:vertex-shader-source material) (:fragment-shader-source material)))

(defn program-of [material-programs material]
  (:program (material-programs (:name material))))

(defn use [gl material]
  (when-not (program-exists? (get-programs) material)
    (add-to-programs! material (create-program gl material)))
  (let [program-of-material (program-of (get-programs) material)]
    (shader-program/use gl program-of-material)))
