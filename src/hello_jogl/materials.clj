(ns hello-jogl.materials)

(defn corn-flower-blue []
  {:vertex-shader-source   "#version 330
                            layout(location = 0) in vec4 in_Position;
                            void main(void)
                            {
                              gl_Position = in_Position;
                            }"

   :fragment-shader-source  "#version 330
                            out vec4 out_Color;
                            void main(void)
                            {
                              out_Color = vec4(0.25f, 0.5f, 0.9f, 1.0f);
                            }"})
