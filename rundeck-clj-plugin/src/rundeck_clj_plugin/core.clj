;;
;; See if  one can implement  a Rundeck Plugin with  Clojure. Probably
;; not without a Java shim.
;;
;;
;; [1] https://docs.rundeck.com/docs/developer/03-step-plugins.html
;; [2] https://github.com/rundeck/rundeck/blob/development/examples/example-java-step-plugin/src/main/java/com/dtolabs/rundeck/plugin/example/ExampleStepPlugin.java
;;
(ns rundeck-clj-plugin.core
  (:import (com.dtolabs.rundeck.plugins.util DescriptionBuilder)
           (com.dtolabs.rundeck.plugins.util PropertyBuilder)))

(defn- get-description []
  (-> (DescriptionBuilder/builder)
      (.name "rundeck_clj_plugin.HelloStepPlugin")
      (.title "Example Step")
      (.description "Does nothing")
      (.property (-> (PropertyBuilder/builder)
                     (.string "bunny")
                     (.title "Bunny")
                     (.description "Bunny name")
                     (.required true)
                     (.build)))
      (.build)))

(defn hello []
  (println "Hello from Clojure!")
  (let [description (get-description)]
    description))

;;
;; I am afraid we need a real Class implementing the interface, not an
;; object or object factory. So that thigs like reify do not qualify.
;;
;; https://www.javadoc.io/doc/org.rundeck/rundeck-core/3.3.3-20200910/com/dtolabs/rundeck/plugins/step/StepPlugin.html
;;
;; executeStep (PluginStepContext context,
;; java.util.Map<java.lang.String, java.lang.Object>
;; configuration)(defn -executeStep [_ context configuration]

(defn -executeStep [_ context configuration]
  (println "I dont do nothing yet!"))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
