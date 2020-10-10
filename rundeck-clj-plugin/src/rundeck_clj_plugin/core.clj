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
;;              java.util.Map<java.lang.String, java.lang.Object> configuration)
;;
;; [1] https://docs.rundeck.com/docs/developer/03-step-plugins.html
;; [2] https://github.com/rundeck/rundeck/blob/development/examples/example-java-step-plugin/src/main/java/com/dtolabs/rundeck/plugin/example/ExampleStepPlugin.java
;;
(ns rundeck-clj-plugin.core
  (:import
   (com.dtolabs.rundeck.plugins.util DescriptionBuilder PropertyBuilder)
   (com.dtolabs.rundeck.plugins.step PluginStepContext)))

(defn get-description [name]
  (println "Hello from Clojure!")
  (-> (DescriptionBuilder/builder)
      (.name name)
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

(defn execute-step [^PluginStepContext context configuration]
  (println "I dont execute much yet!")

  ;; Supplied confuguration is just a java.util.HashMap:
  (println configuration)
  ;; If you prefer to work with Clojure Maps it is one step away:
  (println (into {} configuration))
  ;; But otherwise a HashMap is just as good of a Map:
  (println
   (for [k (keys configuration)]
     {:key k, :value (get configuration k)}))

  ;; The context is a Rundeck Type PluginStepContextImpl:
  (println context)

  ;; It is what it says:
  (println {:step-number (-> context .getStepNumber)})

  ;;
  ;; A Node  in Rundeck  is a glorified  map of  atributes Map<String,
  ;; String> and an extra set of  string-valued tags all buried in the
  ;; Java MBean  legacy.  Node Set  is internally a TreeMap  from node
  ;; names to  such Nodes TreeMap<String, INodeEntry>  albeit obscured
  ;; by its custom  set of accessors hiding the  actual TreeMap behind
  ;; an INodeSet of NodeSetImpl [1].
  ;;
  ;; [1] https://github.com/rundeck/rundeck/blob/main/core/src/main/java/com/dtolabs/rundeck/core/common/NodeSetImpl.java
  ;;

  ;; .getNodeNames returns just the keys, ...
  (println
   {:node-names (seq (-> context .getNodes .getNodeNames))})

  ;; You probably want all of node attributes instead:
  (println
   (let [nodes (.getNodes context)]
     (for [node nodes]
       ;; One is HashMap another is HashSet, thus "plain data" and can
       ;; be used as is from Clojure, actually:
       {:attributes (into {} (.getAttributes node)),
        :tags (into #{} (.getTags node))})))

  ;; .getStepContext is an ArrayList, of what?
  (println
   {:step-context (seq (.getStepContext context))}))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
