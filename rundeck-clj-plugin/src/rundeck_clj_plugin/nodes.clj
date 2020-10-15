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

;; See more example Properties in step.clj and/or Rundeck Docs.
(defn get-description [name]
  (println "get-description: building description ...")
  (-> (DescriptionBuilder/builder)
      (.name name)
      (.title "Hello Nodes")
      (.description "Supplies nodes")
      (.property (-> (PropertyBuilder/builder)
                     (.string "host-group")
                     (.title "Host group")
                     (.description "Identifies the group of hosts")
                     (.required true)
                     (.build)))
      (.property (-> (PropertyBuilder/builder)
                     (.string "user-name")
                     (.title "User name")
                     (.description "Node resources must specify a user name")
                     (.defaultValue "root")
                     (.required true)
                     (.build)))
      (.property (-> (PropertyBuilder/builder)
                     (.integer "node-count")
                     (.title "Node count")
                     (.description "How many?")
                     (.required false)
                     (.defaultValue "2")
                     (.build)))
      (.build)))

(defn- make-node [attributes tags]
  (doto (NodeEntryImpl.)
    ;; Should better be a mutable HashSet:
    (.setTags tags)
    ;; Should better be a mutable HashMap:
    (.setAttributes attributes)))

;; You will  probably want to move  the core functionality out  of the
;; namespaces "tainted" by  Rundeck Classes, even if  just for testing
;; from the CLI or such.
(defn- make-nodes [properties]
  ;; These defaults should  not apply, right? Rundeck  will supply its
  ;; own, coded in Properties, wont it?
  (let [user (get properties "user-name" "root")
        node-count (Long/parseLong (get properties "node-count" "1"))]
    (for [n (range node-count)
          :let [node-name (str (gensym "node-name-"))]]
      (make-node {"name" node-name
                  "nodename" node-name ; obligatory
                  "hostname" (str  "127.0.0." (rand-int 255))
                  "username" user}
                 #{(get ["production" "staging" "test"] (rand-int 3))}))))

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
                        (for [^INodeEntry n (make-nodes properties)]
                          #_[(.getNodename n) n]
                          [(str (gensym)) n]))]
        (NodeSetImpl. (java.util.HashMap. ^java.util.Map nodes))))))

