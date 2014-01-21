(ns hello-jogl.core
  (:gen-class)
  (:import (javax.media.opengl.awt GLCanvas)
           (javax.swing JFrame)
           (javax.media.opengl GLEventListener)
           (javax.media.opengl.glu GLU)
           (java.nio FloatBuffer IntBuffer)
           (com.jogamp.opengl.util FPSAnimator GLBuffers)))

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

(defn vertex-buffer-object [gl vertex-positions]
  (let [vertex-array (int-array 1)
        vertex-buffer (int-array 1)
        position-buffer (GLBuffers/newDirectFloatBuffer (float-array vertex-positions))]
    (doto gl
      (.glGenVertexArrays 1 (IntBuffer/wrap vertex-array))
      (.glBindVertexArray (first vertex-array))
      (.glGenBuffers 1 (IntBuffer/wrap vertex-buffer))
      (.glBindBuffer javax.media.opengl.GL/GL_ARRAY_BUFFER (first vertex-buffer))
      (.glBufferData javax.media.opengl.GL/GL_ARRAY_BUFFER (int (* (count vertex-positions) 4)) position-buffer javax.media.opengl.GL/GL_STATIC_DRAW)
      (.glVertexAttribPointer 0 4 javax.media.opengl.GL/GL_FLOAT false 0 0)
      (.glEnableVertexAttribArray 0))
    (first vertex-array)))

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
    out_Color = vec4(1.0f, 0.0f, 0.0f, 1.0f);
  }")

(def geometry nil)
(def program nil)

(defn create-shader-program [gl vertex-shader-source fragment-shader-source]
  (let [shader-program (.glCreateProgram gl)
        vertex-shader (.glCreateShader gl javax.media.opengl.GL2ES2/GL_VERTEX_SHADER)
        vertex-shader-source (into-array String [vertex-shader-source])
        fragment-shader (.glCreateShader gl javax.media.opengl.GL2ES2/GL_FRAGMENT_SHADER)
        fragment-shader-source (into-array String [fragment-shader-source])]
    (doto gl
      (.glShaderSource vertex-shader 1 vertex-shader-source nil)
      (.glCompileShader vertex-shader)
      (.glShaderSource fragment-shader 1 fragment-shader-source nil)
      (.glCompileShader fragment-shader)
      (.glAttachShader shader-program vertex-shader)
      (.glAttachShader shader-program fragment-shader)
      (.glLinkProgram shader-program))
    shader-program))

(defn on-init [drawable]
  (let [gl (gl-context-of drawable)]
    (doto gl
      (.glClearColor 0 0 0 0)
      (.glClearDepth 1)
      (.glEnable javax.media.opengl.GL/GL_DEPTH_TEST))
    (def geometry (vertex-buffer-object gl positions))
    (def program (create-shader-program gl vs fs))))

(defn on-reshape [drawable x y width height]
  (let [gl (gl-context-of drawable)
        aspect (double (/ width height))]
    (doto gl
      (.glViewport 0 0 width height))))

(defn on-display [drawable]
  (let [gl (gl-context-of drawable)]
    (doto gl
      (.glClear (bit-or javax.media.opengl.GL/GL_COLOR_BUFFER_BIT javax.media.opengl.GL/GL_DEPTH_BUFFER_BIT))
      (.glUseProgram program)
      (.glBindVertexArray geometry)
      (.glDrawArrays javax.media.opengl.GL/GL_TRIANGLES 0 3))))

(defn -main
  [& args]
  (application "Hello world!" 800 600 60 on-init on-reshape on-display (fn [drawable] (println "Dispose"))))