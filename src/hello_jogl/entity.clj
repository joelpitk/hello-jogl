(ns hello-jogl.entity)

(defn has-component? [entity component]
  (contains? entity component))

(defn has-components? [entity & components]
  (every? (fn [component] (has-component? entity component)) components))
