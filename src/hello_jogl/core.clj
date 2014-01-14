(ns hello-jogl.core
  (:gen-class)
  (:import (javax.media.opengl.awt GLCanvas)
           (javax.swing JFrame)
           (javax.media.opengl GLEventListener)
           (javax.media.opengl.glu GLU)
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
  (.getGL2 (.getGL drawable)))

(defn on-init [drawable]
  (let [gl (gl-context-of drawable)]
    (doto gl
      (.glClearColor 0 0 0 0)
      (.glClearDepth 1)
      (.glEnable javax.media.opengl.GL2/GL_DEPTH_TEST))))

(defn on-reshape [drawable x y width height]
  (let [gl (gl-context-of drawable)
        aspect (double (/ width height))]
    (doto gl
      (.glViewport 0 0 width height)
      (.glMatrixMode javax.media.opengl.GL2/GL_PROJECTION)
      (.glLoadIdentity))
    (.gluPerspective (GLU.) 45.0 aspect 0.1 100.0)
    (.glMatrixMode gl javax.media.opengl.GL2/GL_MODELVIEW)))


(def rotation 0)

(defn on-display [drawable]
  (let [gl (gl-context-of drawable)]
    (doto gl
      (.glClear (bit-or javax.media.opengl.GL2/GL_COLOR_BUFFER_BIT javax.media.opengl.GL2/GL_DEPTH_BUFFER_BIT))
      (.glColor3f 1 1 1)
      (.glLoadIdentity)
      (.glTranslatef 0 0 -3)
      (.glRotatef rotation 0 0 1)
      (.glBegin javax.media.opengl.GL2/GL_TRIANGLES)
        (.glColor3f 1 0 0)
        (.glVertex3f  0  1 0)
        (.glColor3f 0 1 0)
        (.glVertex3f -1 -1 0)
        (.glColor3f 0 0 1)
        (.glVertex3f  1 -1 0)
      (.glEnd)))
  (def rotation (+ 0.3 rotation)))

(defn -main
  [& args]
  (application "Hello world!" 800 600 60 on-init on-reshape on-display (fn [drawable] (println "Dispose"))))