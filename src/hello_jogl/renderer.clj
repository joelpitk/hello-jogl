(ns hello-jogl.renderer
  (:require [midje.sweet :refer [unfinished]]
            [hello-jogl.entity :as entity]))


(unfinished render-all)
(unfinished render)

(defn renderable? [entity]
  (entity/has-components? entity :position :geometry))

(defn renderable [entities] nil
  (filter renderable? entities))

(defn render-all [gl entities]
  (doseq [entity entities]
    (render gl entity)))
