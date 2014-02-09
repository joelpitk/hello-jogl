(ns hello-jogl.vertex-array
  (:import (java.nio FloatBuffer IntBuffer)
           (com.jogamp.opengl.util FPSAnimator GLBuffers)))

(defn create [gl geometry]
  (let [vertex-array-objects (int-array 1)
        vertex-buffer-objects (int-array 1)
        index-buffer-objects (int-array 1)
        vertex-buffer-data (GLBuffers/newDirectFloatBuffer (float-array (:vertices geometry)))
        index-buffer-data (GLBuffers/newDirectIntBuffer (int-array (:indices geometry)))
        vertex-buffer-size-in-bytes (int (* (count (:vertices geometry)) 4))
        index-buffer-size-in-bytes (int (* (count (:indices geometry)) 4))]
    (doto gl
      (.glGenVertexArrays 1 (IntBuffer/wrap vertex-array-objects))
      (.glBindVertexArray (first vertex-array-objects))

      (.glGenBuffers 1 (IntBuffer/wrap vertex-buffer-objects))
      (.glBindBuffer javax.media.opengl.GL/GL_ARRAY_BUFFER (first vertex-buffer-objects))
      (.glBufferData javax.media.opengl.GL/GL_ARRAY_BUFFER vertex-buffer-size-in-bytes vertex-buffer-data javax.media.opengl.GL/GL_STATIC_DRAW)

      (.glGenBuffers 1 (IntBuffer/wrap index-buffer-objects))
      (.glBindBuffer javax.media.opengl.GL/GL_ELEMENT_ARRAY_BUFFER (first index-buffer-objects))
      (.glBufferData javax.media.opengl.GL/GL_ELEMENT_ARRAY_BUFFER index-buffer-size-in-bytes index-buffer-data javax.media.opengl.GL/GL_STATIC_DRAW)

      (.glVertexAttribPointer 0 4 javax.media.opengl.GL/GL_FLOAT false 0 0))
    {:vertex-array-object (first vertex-array-objects)
     :vertex-buffer-object (first vertex-buffer-objects)
     :index-buffer-object (first index-buffer-objects)
     :vertex-count (count (:indices geometry))}))

(defn draw [gl vertex-array]
  (doto gl
    (.glBindVertexArray (:vertex-array-object vertex-array))
    (.glEnableVertexAttribArray 0)
    ;(.glDrawArrays javax.media.opengl.GL/GL_TRIANGLES 0 (:vertex-count vertex-array))
    (.glDrawElements javax.media.opengl.GL/GL_TRIANGLES (:vertex-count vertex-array) javax.media.opengl.GL/GL_UNSIGNED_INT 0)
    (.glDisableVertexAttribArray 0)))

(defn delete [gl vertex-array]
  (let [vertex-buffers-to-delete (int-array [(:vertex-buffer-object vertex-array)])
        index-buffers-to-delete (int-array [(:index-buffer-object vertex-array)])
        arrays-to-delete (int-array [(:vertex-array-object vertex-array)])]
    (doto gl
      (.glBindBuffer javax.media.opengl.GL/GL_ARRAY_BUFFER 0)
      (.glDeleteBuffers 1 vertex-buffers-to-delete 0)
      (.glBindBuffer javax.media.opengl.GL/GL_ELEMENT_ARRAY_BUFFER 0)
      (.glDeleteBuffers 1 index-buffers-to-delete 0)
      (.glBindVertexArray 0)
      (.glDeleteVertexArrays 1 arrays-to-delete 0))))
