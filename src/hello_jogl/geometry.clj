(ns hello-jogl.geometry)

(def quad {:vertices [-1.0 1.0 0.0 1.0
                      -1.0 -1.0 0.0 1.0
                       1.0 -1.0 0.0 1.0
                       1.0 1.0 0.0 1.0]
           :indices [0 1 2
                     2 0 3]})
