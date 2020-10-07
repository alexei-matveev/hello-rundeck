;;
;; See if one can implement a Rundeck Plugin with Clojure ...
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
;; import com.dtolabs.rundeck.plugins.step.StepPlugin;
;; import com.dtolabs.rundeck.plugins.util.DescriptionBuilder;
;; import com.dtolabs.rundeck.plugins.util.PropertyBuilder;
;; import java.util.Map;

;; Wau this is getting ugly so quick ...
(ns rundeck-clj-plugin.core
  ;; https://kotka.de/blog/2010/02/gen-class_how_it_works_and_how_to_use_it.html
  (:gen-class
   ;; @Plugin(name = ExampleStepPlugin.SERVICE_PROVIDER_NAME, service = ServiceNameConstants.WorkflowStep)
   :name ^{com.dtolabs.rundeck.core.plugins.Plugin
           {:name "rundeck_clj_plugin.core", :service "WorkflowStep"}}
   rundeck_clj_plugin.core
   :implements [com.dtolabs.rundeck.plugins.step.StepPlugin]))

;;
;; I am afraid we need a real Class implementing the interface, not an
;; object or object factory. So that thigs like reify do not qualify.
;;
;; https://www.javadoc.io/doc/org.rundeck/rundeck-core/3.3.3-20200910/com/dtolabs/rundeck/plugins/step/StepPlugin.html
;;
;; executeStep (PluginStepContext context,
;; java.util.Map<java.lang.String, java.lang.Object>
;; configuration)(defn -executeStep [_ context configuration]

(defn -executeStep [_ context configuration]
  (println "I dont do nothing yet!"))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [x com.dtolabs.rundeck.plugins.ServiceNameConstants/WorkflowStep]
    (println (type x))
    (println x))
  (println "Hello, World!"))
