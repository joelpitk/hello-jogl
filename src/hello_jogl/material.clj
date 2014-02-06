(ns hello-jogl.material
  (:refer-clojure :exclude [use])
  (:require [midje.sweet :refer [unfinished]]
            [hello-jogl.shader-program :as shader-program]))

(unfinished get-programs create-program add-to-programs! program-of)

(defn use [gl material]
  (when-not (program-exists? (get-programs) material)
    (add-to-programs! material (create-program gl material)))
  (shader-program/use gl (program-of (get-programs) material)))

(defn program-exists? [programs material]
  (contains? programs material))
