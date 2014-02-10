(ns hello-jogl.core
  (:gen-class)
  (:require [hello-jogl [shader-program :as shader-program]
                        [vertex-array :as vertex-array]
                        [renderer :as renderer]
                        [entity :as entity]
                        [materials :as materials]
                        [geometry :as geometry]])
  (:import (javax.media.opengl.awt GLCanvas)
           (javax.swing JFrame)
           (javax.media.opengl GLEventListener)
           (com.jogamp.opengl.util FPSAnimator)))

(defn gl-listener [init-action reshape-action display-action dispose-action]
  (let [gl-context-of (fn [drawable] (.getGL3 (.getGL drawable)))
        listener (proxy [GLEventListener] []
                   (init [drawable] (init-action (gl-context-of drawable)))
                   (reshape [drawable x y width height] (reshape-action (gl-context-of drawable) x y width height))
                   (display [drawable] (display-action (gl-context-of drawable)))
                   (dispose [drawable] (dispose-action (gl-context-of drawable))))] listener))

(defn application [title width height target-framerate init-action reshape-action display-action cleanup-action]
  (let [frame (JFrame. title)
        canvas (GLCanvas.)
        listener (gl-listener init-action reshape-action display-action cleanup-action)]
    (.addGLEventListener canvas listener)
    (.add (.getContentPane frame) canvas)
    (doto frame (.setSize width height) (.setVisible true) (.setDefaultCloseOperation JFrame/EXIT_ON_CLOSE))
    (.start (FPSAnimator. canvas target-framerate true))))

(def program nil)

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
  (application "Hello world!" 800 600 60 on-init on-reshape on-display on-dispose))

(comment (-main))
