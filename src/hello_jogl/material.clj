(ns hello-jogl.material
  (:refer-clojure :exclude [use])
  (:require [midje.sweet :refer [unfinished]]
            [hello-jogl.shader-program :as shader-program]))

(def material-programs (atom {}))

(defn get-programs []
  @material-programs)

(defn add-to-programs! [material program]
  (reset! material-programs (assoc @material-programs (:name material) {:material material :program program})))

(defn update-programs! [material program]
  (reset! material-programs (update-in @material-programs [(:name material)] (constantly {:material material :program program}))))

(defn program-exists? [programs material]
  (contains? programs (:name material)))

(defn create-program [gl material]
  (shader-program/create gl (:vertex-shader-source material) (:fragment-shader-source material)))

(defn program-of [material-programs material]
  (:program (material-programs (:name material))))

(defn get-material-by-name [material-programs name-of-material]
  (:material (material-programs name-of-material)))

(defn source-changed? [material original-material]
  (let [vertex-shader-source (:vertex-shader-source material)
        fragment-shader-source (:fragment-shader-source material)
        original-vertex-shader-source (:vertex-shader-source original-material)
        original-fragment-shader-source (:fragment-shader-source original-material)]
  (or (not= vertex-shader-source original-vertex-shader-source) (not= fragment-shader-source original-fragment-shader-source))))

(defn use [gl material]
  (when-not (program-exists? (get-programs) material)
    (add-to-programs! material (create-program gl material)))
  (let [program-of-material (program-of (get-programs) material)
        original-material (get-material-by-name (get-programs) (:name material))]
    (when (source-changed? material original-material)
      (update-programs! material (shader-program/recompile gl program-of-material (:vertex-shader-source material) (:fragment-shader-source material))))
    (shader-program/use gl program-of-material)))
