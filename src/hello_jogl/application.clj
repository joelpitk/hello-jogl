(ns hello-jogl.application
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

(defn create [title width height target-framerate init-action reshape-action display-action cleanup-action]
  (let [frame (JFrame. title)
        canvas (GLCanvas.)
        listener (gl-listener init-action reshape-action display-action cleanup-action)]
    (.addGLEventListener canvas listener)
    (.add (.getContentPane frame) canvas)
    (doto frame (.setSize width height) (.setVisible true) (.setDefaultCloseOperation JFrame/EXIT_ON_CLOSE))
    (.start (FPSAnimator. canvas target-framerate true))))
