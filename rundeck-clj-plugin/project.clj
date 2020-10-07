(defproject rundeck-clj-plugin "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 #_[org.rundeck/rundeck-core "3.3.3-20200910"]]
  :main ^:skip-aot rundeck-clj-plugin.core
  :target-path "target/%s"
  :profiles {:provided {:dependencies
                        [[org.rundeck/rundeck-core "3.3.3-20200910"]]}
             :uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}}
  ;; https://docs.rundeck.com/docs/developer/01-plugin-development.html#java-plugin-development
  :uberjar-name "rundeck-clj-plugin-0.1.0.jar"
  :manifest {"Rundeck-Plugin-Version" "1.2"
             "Rundeck-Plugin-Archive" "true"
             "Rundeck-Plugin-Classnames" "rundeck_clj_plugin.core"
             "Rundeck-Plugin-Libs" ""
             "Rundeck-Plugin-Author" "f0bec0d"
             "Rundeck-Plugin-URL" "https://xxx.yyy"
             "Rundeck-Plugin-Date" "2020-10-07"
             "Rundeck-Plugin-File-Version" "20201007"})

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
