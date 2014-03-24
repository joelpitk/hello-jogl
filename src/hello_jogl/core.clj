(ns hello-jogl.core
  (:gen-class)
  (:require [hello-jogl [shader-program :as shader-program]
                        [vertex-array :as vertex-array]
                        [renderer :as renderer]
                        [entity :as entity]
                        [materials :as materials]
                        [geometry :as geometry]
                        [application :as application]]))

(def world {:entities [{:geometry geometry/quad
                        :material materials/corn-flower-blue}]})

(defn on-init [gl]
  (doto gl
    (.glClearColor 0 0 0 0)
    (.glClearDepth 1)
    (.glEnable javax.media.opengl.GL/GL_DEPTH_TEST)))

(defn on-reshape [gl x y width height]
  (let [aspect (double (/ width height))]
    (doto gl
      (.glViewport 0 0 width height))))

(defn draw [gl entities]
  (renderer/render-all gl entities))

(defn on-display [gl]
  (.glClear gl (bit-or javax.media.opengl.GL/GL_COLOR_BUFFER_BIT javax.media.opengl.GL2/GL_DEPTH_BUFFER_BIT))
  (draw gl (:entities world)))

(defn on-dispose [gl]
  (renderer/dispose gl))

(defn -main
  [& args]
  (application/create "Hello world!" 800 600 60 on-init on-reshape on-display on-dispose))

(comment (-main))
