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
   (com.dtolabs.rundeck.plugins.step PluginStepContext)
   (com.dtolabs.rundeck.core.execution.workflow.steps StepException FailureReason)
   ;; We wont  really need  to construct  nodes here,  just understand
   ;; them good enough:
   (com.dtolabs.rundeck.core.common INodeEntry NodeEntryImpl NodeSetImpl)))

;; To make Description for a Describable Plugin you may consider using
;; this  Builder pattern.   There are  instances for  method ".values"
;; taking  a  List  and  a variadic  instance.   Use  (.values  [...])
;; because  a vector  is a  java List  too but  calling variadic  Java
;; functions from Clojure can be a pain.
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
      (.property (-> (PropertyBuilder/builder)
                     (.freeSelect "color")
                     (.title "Color")
                     (.description "Your color")
                     (.required false)
                     (.defaultValue "Blue")
                     (.values ["Blue", "Beige", "Black"])
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
      (.property (-> (PropertyBuilder/builder)
                     (.select "rice")
                     (.title "Rice Cream")
                     (.description "Rice Cream Flavor")
                     (.required false)
                     (.values ["Flambe", "Crambo"])
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

  ;; .getNodeNames returns just the keys, ...
  (println
   {:node-names (seq (-> context .getNodes .getNodeNames))})

  ;; You probably want all of node attributes instead:
  (println
   (let [nodes (.getNodes context)]
     (for [^INodeEntry node nodes]
       ;; One is a HashMap another is a HashSet, thus "plain data" and
       ;; can be used as is from Clojure, actually:
       {:attributes (into {} (.getAttributes node)),
        :tags (into #{} (.getTags node))})))

  ;; .getStepContext is an ArrayList, of what?
  (println
   {:step-context (seq (.getStepContext context))})

  ;; This is how one is supposed to  fail. For some reason there was a
  ;; requirement for the toString method to return "reason". I was not
  ;; willing to check what happens otherweise.
  (if (= "true" (get configuration "lampkin"))
    (throw (StepException. "lampkin was true in clojure"
                           (reify FailureReason
                             (toString [_] "reason"))))))

;;
;; A Node in  Rundeck is a glorified map  of atributes HashMap<String,
;; String> and  an extra HashSet  of string-valued tags all  buried in
;; the Java MBean  legacy [1].  Node Set is internally  a TreeMap from
;; node  names  to  such   Nodes  TreeMap<String,  INodeEntry>  albeit
;; obscured by its  custom set of accessors hiding  the actual TreeMap
;; behind an INodeSet of NodeSetImpl [2].
;;
;; Attributes  as a  *persistent* Map  does not  allow Put-Operations,
;; thus if  you try to  .setTags after .setAttributes you  will notice
;; it. Setting tags  has namely the additional effect  of inserting an
;; attribute "tags" which  is a comma separated list  derived from the
;; set.   Though  here  we  appear  to  overwrite  that  list  with  a
;; subsequent .setAttributes.  Go figure.   Do not use the *attribute*
;; "tags".  FWIW, the order in the example is reversed [3].
;;
;; For e reasonable example of a Resource Model Source Plugin see "EC2
;; Nodes Plugin" [4].
;;
;; [1] https://github.com/rundeck/rundeck/blob/main/core/src/main/java/com/dtolabs/rundeck/core/common/NodeEntryImpl.java
;; [2] https://github.com/rundeck/rundeck/blob/main/core/src/main/java/com/dtolabs/rundeck/core/common/NodeSetImpl.java
;; [3] https://github.com/rundeck/rundeck/blob/development/examples/json-plugin/src/main/java/com/dtolabs/rundeck/plugin/resources/format/json/JsonResourceFormatParser.java
;; [4] https://github.com/rundeck-plugins/rundeck-ec2-nodes-plugin
;;
(defn- demo-make-node []
  (let [node (doto (NodeEntryImpl.)
               ;; Should rather be a mutable HashSet:
               (.setTags #{"critical" "to be removed"})
               ;; Should rather be a mutable HashMap:
               (.setAttributes {"name" "localhost", "hostid" "1234"}))]
    {:node node
     :tags (.getTags node)
     :attributes (.getAttributes node)}))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
