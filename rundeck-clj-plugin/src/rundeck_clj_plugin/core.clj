;;
;; See if  one can implement  a Rundeck Plugin with  Clojure. Probably
;; not without a Java shim.
;;
;; We need a real Class implementing Rundeck interfaces, not an object
;; or   object   factory.   So   that  thigs   like   reify   do   not
;; qualify. Moreover Rundeck apparently requires @Plugin annotation on
;; such Plugin Classes aka Providers. Sigh ...
;;
;; https://www.javadoc.io/doc/org.rundeck/rundeck-core/3.3.3-20200910/com/dtolabs/rundeck/plugins/step/StepPlugin.html
;;
;; executeStep (PluginStepContext context,
;; java.util.Map<java.lang.String, java.lang.Object>
;; configuration)(defn -executeStep [_ context configuration]
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

(defn execute-step [context configuration]
  (println "I dont execute much yet!")

  ;; Supplied confuguration is just a java.util.HashMap, if you prefer
  ;; to work with Clojure Maps it is one step away:
  (println (type configuration))
  (println configuration)
  (println (into {} configuration))
  (for [k (keys configuration)]
    (println {:key k, :value (get configuration k)}))

  ;; The context is a Rundeck Type PluginStepContextImpl:
  (println (type context))
  (println context)

  ;; System.out.println("Example step executing on nodes: " + context.getNodes().getNodeNames());
  ;; System.out.println("Example step num: " + context.getStepNumber());
  ;; System.out.println("Example step context: " + context.getStepContext());

  (println {:step-number (-> context .getStepNumber)})
  ;; .getNodeNames returns a TreeMap:
  (println {:nodes (seq (-> context .getNodes .getNodeNames))})
  ;; .getStepContext is an ArrayList:
  (println {:step-context (seq (-> context .getStepContext))}))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
