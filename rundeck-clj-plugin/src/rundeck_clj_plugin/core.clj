;;
;; See if  one can implement  a Rundeck Plugin with  Clojure. Probably
;; not without a Java shim.
;;
;;
;; [1] https://docs.rundeck.com/docs/developer/03-step-plugins.html
;; [2] https://github.com/rundeck/rundeck/blob/development/examples/example-java-step-plugin/src/main/java/com/dtolabs/rundeck/plugin/example/ExampleStepPlugin.java
;;
(ns rundeck-clj-plugin.core
  (:import
   (com.dtolabs.rundeck.plugins.util DescriptionBuilder PropertyBuilder)))

(defn get-description []
  (println "Hello from Clojure!")
  (-> (DescriptionBuilder/builder)
      (.name "rundeck_clj_plugin.HelloStepPlugin")
      (.title "Hello Step")
      (.description "Does nothing useful")
      (.property (-> (PropertyBuilder/builder)
                     (.string "bunny")
                     (.title "Bunny")
                     (.description "Bunny name")
                     (.required true)
                     (.build)))
      (.property (-> (PropertyBuilder/builder)
                     (.booleanType "lampkin")
                     (.title "Lampkin")
                     (.description "Want Lampkin?")
                     (.required false)
                     (.defaultValue "false")
                     (.build)))
      #_(.property (-> (PropertyBuilder/builder)
                     (.freeSelect "color")
                     (.title "Color")
                     (.description "Your color")
                     (.required false)
                     (.defaultValue "Blue")
                     (.values (into-array String ["Blue", "Beige", "Black"]))
                     (.build)))
      (.property (-> (PropertyBuilder/builder)
                     (.integer "many")
                     (.title "Many")
                     (.description "How many?")
                     (.required false)
                     (.defaultValue "2")
                     (.build)))
      (.property (-> (PropertyBuilder/builder)
                     (.longType "cramp")
                     (.title "Cramp")
                     (.description "How crampy more?")
                     (.required false)
                     (.defaultValue "20")
                     (.build)))
      #_(.property (-> (PropertyBuilder/builder)
                     (.select "rice")
                     (.title "Rice Cream")
                     (.description "Rice Cream Flavor")
                     (.required false)
                     (.values (into-array String ["Flambe", "Crambo"]))
                     (.build)))
      (.build)))

;;
;; I am afraid we need a real Class implementing the interface, not an
;; object or object factory. So that thigs like reify do not qualify.
;;
;; https://www.javadoc.io/doc/org.rundeck/rundeck-core/3.3.3-20200910/com/dtolabs/rundeck/plugins/step/StepPlugin.html
;;
;; executeStep (PluginStepContext context,
;; java.util.Map<java.lang.String, java.lang.Object>
;; configuration)(defn -executeStep [_ context configuration]

(defn execute-step [context configuration]
  (println "I dont execute anything yet!"))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
