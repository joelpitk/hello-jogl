(ns hello-jogl.vertex-array
  (:import (java.nio FloatBuffer IntBuffer)
           (com.jogamp.opengl.util FPSAnimator GLBuffers)))

(defn create [gl vertex-positions]
  (let [vertex-array-objects (int-array 1)
        vertex-buffer-objects (int-array 1)
        vertex-position-buffer (GLBuffers/newDirectFloatBuffer (float-array vertex-positions))]
    (doto gl
      (.glGenVertexArrays 1 (IntBuffer/wrap vertex-array-objects))
      (.glBindVertexArray (first vertex-array-objects))
      (.glGenBuffers 1 (IntBuffer/wrap vertex-buffer-objects))
      (.glBindBuffer javax.media.opengl.GL/GL_ARRAY_BUFFER (first vertex-buffer-objects))
      (.glBufferData javax.media.opengl.GL/GL_ARRAY_BUFFER (int (* (count vertex-positions) 4)) vertex-position-buffer javax.media.opengl.GL/GL_STATIC_DRAW)
      (.glVertexAttribPointer 0 4 javax.media.opengl.GL/GL_FLOAT false 0 0))
    {:vertex-array-object (first vertex-array-objects)
     :vertex-buffer-object (first vertex-buffer-objects)
     :vertex-count (/ (count vertex-positions) 4)}))

(defn draw [gl vertex-array]
  (doto gl
    (.glBindVertexArray (:vertex-array-object vertex-array))
    (.glEnableVertexAttribArray 0)
    (.glDrawArrays javax.media.opengl.GL/GL_TRIANGLES 0 (:vertex-count vertex-array))
    (.glDisableVertexAttribArray 0)))

(defn delete [gl vertex-array]
  (let [buffers-to-delete (int-array [(:vertex-buffer-object vertex-array)])
        arrays-to-delete  (int-array [(:vertex-array-object vertex-array)])]
    (doto gl
      (.glBindBuffer javax.media.opengl.GL/GL_ARRAY_BUFFER 0)
      (.glDeleteBuffers 1 buffers-to-delete 0)
      (.glBindVertexArray 0)
      (.glDeleteVertexArrays 1 arrays-to-delete 0))))
