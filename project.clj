(defproject hello-jogl "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojars.toxi/jogl "2.0.0-rc11"]
                 [org.clojure/algo.generic "0.1.1"]
                 [net.mikera/vectorz-clj "0.19.0"]]
  :profiles {:dev {:plugins [[lein-midje "3.1.1"]]
                   :dependencies [[midje "1.5.1"]]}}
  :main hello-jogl.core)
