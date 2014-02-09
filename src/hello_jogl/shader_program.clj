(ns hello-jogl.shader-program
  (:refer-clojure :exclude [use]))

(defn create [gl vertex-shader-source fragment-shader-source]
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
    (let [program {:program shader-program :vertex-shader vertex-shader :fragment-shader fragment-shader}]
      (println (str "Created program:" program))
      program)))

(defn use [gl shader-program]
  (.glUseProgram gl (:program shader-program)))

(defn recompile [gl shader-program vertex-shader-source fragment-shader-source]
  (let [program (:program shader-program)
        vertex-shader  (:vertex-shader shader-program)
        vertex-shader-source (into-array String [vertex-shader-source])
        fragment-shader (:fragment-shader shader-program)
        fragment-shader-source (into-array String [fragment-shader-source])]
    (doto gl
      (.glUseProgram program)
      (.glShaderSource vertex-shader 1 vertex-shader-source nil)
      (.glCompileShader vertex-shader)
      (.glShaderSource fragment-shader 1 fragment-shader-source nil)
      (.glCompileShader fragment-shader)
      (.glLinkProgram program))
    (let [program {:program program :vertex-shader vertex-shader :fragment-shader fragment-shader}]
      (println (str "Recompiled program:" program))
      program)))

(defn delete [gl shader-program]
  (let [{program-to-delete :program
         vertex-shader-to-delete :vertex-shader
         fragment-shader-to-delete :fragment-shader} shader-program]
  (doto gl
    (.glUseProgram 0)
    (.glDetachShader program-to-delete vertex-shader-to-delete)
    (.glDetachShader program-to-delete fragment-shader-to-delete)
    (.glDeleteShader vertex-shader-to-delete)
    (.glDeleteShader fragment-shader-to-delete)
    (.glDeleteProgram program-to-delete))
    (println "Deleted program:" shader-program)))
