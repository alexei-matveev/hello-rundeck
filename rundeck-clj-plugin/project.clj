(defproject rundeck-clj-plugin "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]]
  :java-source-paths ["src/java"]
  :main ^:skip-aot rundeck-clj-plugin.core
  :target-path "target/%s"
  ;;
  ;; Rundeck Core Classes are provided  by Framework --- do no include
  ;; them  in  your  Uberjar.   The  version  here  should  better  be
  ;; compatible  with that  in  k3s  Deployment entsprechen.   Consult
  ;; Docker Hub  and the Developer  manual [1]. Documentation  may lag
  ;; behind, so check Maven directly [2]:
  ;;
  ;; [1] https://docs.rundeck.com/docs/developer/01-plugin-development.html#java-plugin-development
  ;; [2] https://search.maven.org/artifact/org.rundeck/rundeck-core
  ;;
  :profiles {:provided {:dependencies
                        [[org.rundeck/rundeck-core "3.3.4-20201007"]]}
             :uberjar {:jvm-opts ["-Dclojure.compiler.direct-linking=true"]}}
  ;; https://docs.rundeck.com/docs/developer/01-plugin-development.html#java-plugin-development
  :jar-name "rundeck-clj-plugin-0.1.0.jar"
  :uberjar-name "rundeck-clj-plugin-0.1.0.jar"
  :manifest {"Rundeck-Plugin-Version" "1.2"
             "Rundeck-Plugin-Archive" "true"
             ;; rundeck_clj_plugin.core didnt quite work, see below:
             "Rundeck-Plugin-Classnames" "rundeck_clj_plugin.HelloStepPlugin"
             "Rundeck-Plugin-Libs" ""
             ;; "Class-Path" ""
             "Rundeck-Plugin-Author" "f0bec0d"
             "Rundeck-Plugin-URL" "https://xxx.yyy"
             "Rundeck-Plugin-Date" "2020-10-08"
             "Rundeck-Plugin-File-Version" "202010090001"})

;;
;; See the discussion of class loaders on Slack:
;;
;;     https://clojurians-log.clojureverse.org/clojure/2020-02-03
;;
;; Even  with  a  Java  shim   as  in  ExampleStepPlugin  loading  the
;; clojure/core stub fails with:
;;
;;    Could not locate clojure/core__init.class, ...
;;
;; It is  just that  the call  chain that  leads to  this clojure/core
;; "stub"  ist started  bei  initializing clojure.java.api.Clojure  in
;; this Java Code:
;;
;;    IFn require = Clojure.var ("clojure.core", "require");
;;
;; This is from the middle of the call stack:
;;
;; Caused by: java.io.FileNotFoundException: Could not locate clojure/core__init.class, clojure/core.clj or clojure/core.cljc on classpath.
;; at clojure.lang.RT.load (RT.java:462) ~[?:?]
;; at clojure.lang.RT.load (RT.java:424) ~[?:?]
;; at clojure.lang.RT.<clinit> (RT.java:338) ~[?:?]
;; at clojure.java.api.Clojure.<clinit> (Clojure.java:97) ~[?:?]
;; at rundeck_clj_plugin.ExampleStepPlugin.hello (ExampleStepPlugin.java:70) ~[?:?]
;; at rundeck_clj_plugin.ExampleStepPlugin.getDescription (ExampleStepPlugin.java:90) ~[?:?]
;;
;; The first attempt without Java  Shim failed too. The speculation is
;; that the  Clojure Class "stab"  generated with gen-class  clause in
;; the  namespace uses  its own  class loader  that cannot  locate the
;; actual init code in  clojure/core__init.class. And despite the fact
;; that          both         rundeck_clj_plugin/core.class          &
;; rundeck_clj_plugin/core__init.class are  next to each other  in the
;; JAR the  (Clojure) class loader  fails. Rundeck appears to  do some
;; non-trivial voodoo with the content of the JAR plugins --- you will
;; find "cache"  directories with Rundeck-Plugin-Libs unpacked  in the
;; filesystem. No wonder the Clojure class loader fails.
;;
;; Rundeck prints a huge Stack  Trace after Uploading the plugin. This
;; is a part of it:
;;
;; Caused by: java.lang.ExceptionInInitializerError
;; at clojure.lang.Namespace.<init> (Namespace.java:34)
;; at clojure.lang.Namespace.findOrCreate (Namespace.java:176)
;; at clojure.lang.Var.internPrivate (Var.java:156)
;; at rundeck_clj_plugin.core.<clinit> (Unknown Source)
;; at java.base/java.lang.Class.forName0 (Native Method)
;; at java.base/java.lang.Class.forName (Class.java:398)
;; at com.dtolabs.rundeck.core.plugins.JarPluginProviderLoader.loadClass (JarPluginProviderLoader.java:435)
;; ... 126 more
;; Caused by: java.io.FileNotFoundException: Could not locate clojure/core__init.class, clojure/core.clj or clojure/core.cljc on classpath.
;; at clojure.lang.RT.load (RT.java:462)
;; at clojure.lang.RT.load (RT.java:424)
;; at clojure.lang.RT.<clinit> (RT.java:338)
;; ... 133 more
;; com.dtolabs.rundeck.core.plugins.PluginException: Error loading class: rundeck_clj_plugin.core
;; at com.dtolabs.rundeck.core.plugins.JarPluginProviderLoader.loadClass (JarPluginProviderLoader.java:440)
