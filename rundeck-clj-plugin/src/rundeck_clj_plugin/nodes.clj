;;
;; See HelloNodes.java for the Java Shim ...
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
(ns rundeck-clj-plugin.nodes
  (:import
   (com.dtolabs.rundeck.plugins.util DescriptionBuilder PropertyBuilder)
   (com.dtolabs.rundeck.core.common INodeEntry NodeEntryImpl NodeSetImpl)
   (com.dtolabs.rundeck.core.resources ResourceModelSource)))

;; To make Description for a Describable Plugin you may consider using
;; this  Builder pattern.   There are  instances for  method ".values"
;; taking  a  List  and  a variadic  instance.   Use  (.values  [...])
;; because  a vector  is a  java List  too but  calling variadic  Java
;; functions from Clojure can be a pain.
(defn get-description [name]
  (println "Hello from Clojure!")
  (-> (DescriptionBuilder/builder)
      (.name name)
      (.title "Hello Nodes")
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

(defn- make-node [attributes tags]
  (doto (NodeEntryImpl.)
    ;; Should better be a mutable HashSet:
    (.setTags tags)
    ;; Should better be a mutable HashMap:
    (.setAttributes attributes)))

(defn- make-nodes []
  [(make-node {"name" "Just-Name",
               "nodename" "Node-Name"   ; obligatory
               "hostname" "127.0.0.42"
               "username" "rOOt"}
              #{"critical" "test"})
   (make-node {"name" "Another-Name",
               "nodename" "Node-Name-2"
               "hostname" "127.0.0.99"
               "username" "r00t"}
              #{"production"})])

(defn create-resource-model-source [properties]
  (println "create-resource-model-source: building resource model source ...")
  (println {:properties properties})
  (reify ResourceModelSource
    ;; Should return an INodeSet:
    (getNodes [_]
      (println "getNodes: just a few in a HashMap ...")
      ;; Note sure  if the  keys are ever  used, generate  some random
      ;; symbols:
      (let [nodes (into {}
                        (for [^INodeEntry n (make-nodes)]
                          #_[(.getNodename n) n]
                          [(str (gensym)) n]))]
        (NodeSetImpl. (java.util.HashMap. ^java.util.Map nodes))))))

