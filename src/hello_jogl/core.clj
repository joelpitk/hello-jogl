(ns hello-jogl.core
  (:gen-class)
  (:require (hello-jogl [shader-program :as shader-program]
                        [vertex-array :as vertex-array]))
  (:import (javax.media.opengl.awt GLCanvas)
           (javax.swing JFrame)
           (javax.media.opengl GLEventListener)
           (com.jogamp.opengl.util FPSAnimator)))

(defn gl-listener [init-action reshape-action display-action dispose-action]
  (let [listener (proxy [GLEventListener] []
                   (init [drawable] (init-action drawable))
                   (reshape [drawable x y width height] (reshape-action drawable x y width height))
                   (display [drawable] (display-action drawable))
                   (dispose [drawable] (dispose-action drawable)))] listener))

(defn application [title width height target-framerate init-action reshape-action display-action cleanup-action]
  (let [frame (JFrame. title)
        canvas (GLCanvas.)
        listener (gl-listener init-action reshape-action display-action cleanup-action)]
    (.addGLEventListener canvas listener)
    (.add (.getContentPane frame) canvas)
    (doto frame (.setSize width height) (.setVisible true) (.setDefaultCloseOperation JFrame/EXIT_ON_CLOSE))
    (.start (FPSAnimator. canvas target-framerate true))))

(defn gl-context-of [drawable]
  (.getGL4 (.getGL drawable)))

(def positions [ 0.0  1.0 0.0 1.0
                -1.0 -1.0 0.0 1.0
                 1.0 -1.0 0.0 1.0])
(def vs
  "#version 400
  layout(location = 0) in vec4 in_Position;
  void main(void)
  {
    gl_Position = in_Position;
  }")

(def fs
  "#version 400
  out vec4 out_Color;
  void main(void)
  {
    out_Color = vec4(1.0f, 0.5f, 0.0f, 1.0f);
  }")

(def geometry nil)
(def program nil)

(defn on-init [drawable]
  (let [gl (gl-context-of drawable)]
    (doto gl
      (.glClearColor 0 0 0 0)
      (.glClearDepth 1)
      (.glEnable javax.media.opengl.GL/GL_DEPTH_TEST))
    (def geometry (vertex-array/create gl positions))
    (def program (shader-program/create gl vs fs))))

(defn on-reshape [drawable x y width height]
  (let [gl (gl-context-of drawable)
        aspect (double (/ width height))]
    (doto gl
      (.glViewport 0 0 width height))))

(defn on-display [drawable]
  (let [gl (gl-context-of drawable)]
    (shader-program/use gl program)
    (.glClear gl (bit-or javax.media.opengl.GL/GL_COLOR_BUFFER_BIT javax.media.opengl.GL2/GL_DEPTH_BUFFER_BIT))
    (vertex-array/draw gl geometry)))

(defn on-dispose [drawable]
  (let [gl (gl-context-of drawable)]
    (shader-program/delete gl program)
    (vertex-array/delete gl geometry)))

(defn -main
  [& args]
  (application "Hello world!" 800 600 60 on-init on-reshape on-display on-dispose))