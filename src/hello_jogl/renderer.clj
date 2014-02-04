(ns hello-jogl.renderer
  (:require [midje.sweet :refer [unfinished]]
            [hello-jogl.entity :as entity]))


(unfinished render-all)

(defn renderable? [entity]
  (entity/has-components? entity :position :geometry))

(defn renderable [entities] nil
  (filter renderable? entities))

