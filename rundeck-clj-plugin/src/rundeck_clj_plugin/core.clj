;;
;; See if one can reify a Rundeck Plugin with Clojure ...
;;
;;
;; [1] https://docs.rundeck.com/docs/developer/03-step-plugins.html
;; [2] https://github.com/rundeck/rundeck/blob/development/examples/example-java-step-plugin/src/main/java/com/dtolabs/rundeck/plugin/example/ExampleStepPlugin.java
;;

;; import com.dtolabs.rundeck.core.execution.workflow.steps.FailureReason;
;; import com.dtolabs.rundeck.core.execution.workflow.steps.StepException;
;; import com.dtolabs.rundeck.core.plugins.Plugin;
;; import com.dtolabs.rundeck.core.plugins.configuration.Describable;
;; import com.dtolabs.rundeck.core.plugins.configuration.Description;
;; import com.dtolabs.rundeck.plugins.ServiceNameConstants;
;; import com.dtolabs.rundeck.plugins.step.PluginStepContext;
;; ->import com.dtolabs.rundeck.plugins.step.StepPlugin;
;; import com.dtolabs.rundeck.plugins.util.DescriptionBuilder;
;; import com.dtolabs.rundeck.plugins.util.PropertyBuilder;
;; import java.util.Map;

(ns rundeck-clj-plugin.core
  (:import (com.dtolabs.rundeck.plugins.step StepPlugin))
  (:gen-class))

;;
;; FIXME: I am afraid we need a real Class implementing the interface,
;; not an object or object factory ...
;;
;; https://www.javadoc.io/doc/org.rundeck/rundeck-core/3.3.3-20200910/com/dtolabs/rundeck/plugins/step/StepPlugin.html
(def ^:private plugin
  (reify StepPlugin
    ;; executeStep (PluginStepContext context,
    ;; java.util.Map<java.lang.String, java.lang.Object>
    ;; configuration)
    (executeStep [_ context configuration]
      (println "I dont do nothing yet!"))))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
