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
