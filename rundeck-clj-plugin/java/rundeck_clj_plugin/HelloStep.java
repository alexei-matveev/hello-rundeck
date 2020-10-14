// Copyright 2012 DTO Labs, Inc. (http://dtolabs.com)
//
// Licensed under the Apache License, Version 2.0 (the "License").
//
// HelloStep demonstrates  a basic StepPlugin.  Building  the plugin's
// Properties to be  exposed in the Rundeck GUI has  been delegated to
// Clojure code.
//
// The plugin  class is annotated  with @Plugin to define  the service
// and name  of this service provider  plugin. As I understood  it ---
// this is a must.
//
// The provider name of this plugin statically defined in the
// class. The service name makes use of ServiceNameConstants to
// provide the known Rundeck service names.
//
// Original Author: Greg Schueler <greg@dtosolutions.com>, Created:
// 11/9/12 4:09 PM

// The same namespace as in the clojure  code. That is a choice, not a
// requirement:
package rundeck_clj_plugin;

import com.dtolabs.rundeck.core.execution.workflow.steps.StepException;
import com.dtolabs.rundeck.core.plugins.Plugin;
import com.dtolabs.rundeck.core.plugins.configuration.Describable;
import com.dtolabs.rundeck.core.plugins.configuration.Description;
import com.dtolabs.rundeck.plugins.ServiceNameConstants;
import com.dtolabs.rundeck.plugins.step.PluginStepContext;
import com.dtolabs.rundeck.plugins.step.StepPlugin;

import java.util.Map;

import clojure.java.api.Clojure;
import clojure.lang.IFn;

@Plugin(name = HelloStep.NAME, service = ServiceNameConstants.WorkflowStep)
public class HelloStep implements StepPlugin, Describable {
    // Define a name  used to identify your plugin. It  is a good idea
    // to use a fully qualified package-style name.
    public static final String NAME = "rundeck_clj_plugin.HelloStep";
    static final String ns = "rundeck-clj-plugin.core";

    //
    // Trying to  call Clojure  from here without  the voodo  with the
    // Class Loader will fail  at loading clojure/core__init.class ---
    // the loader just does not find it.   Please do not ask. I do not
    // quite get  it.  Rundeck  does some  non-trivial staff  with the
    // class files, jars, and the class loader so that Clojure ist not
    // able to bootstrap itself without this voodoo [1]:
    //
    //     Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
    //
    // [1] https://groups.google.com/forum/#!msg/clojure/Aa04E9aJRog/f0CXZCN1z0AJ
    //
    public HelloStep () {
        Thread.currentThread()
            .setContextClassLoader (HelloStep.class.getClassLoader());

        // Load the namespace once, it will be used in at least two
        // methods:
        IFn require = Clojure.var ("clojure.core", "require");
        require.invoke (Clojure.read (ns));
    }

    // Overriding this method  gives the plugin a chance  to take part
    // in  building the  Description presented  by this  plugin.  This
    // subclass can  use the DescriptionBuilder to  modify all aspects
    // of the description, add or remove properties, etc.
    public Description getDescription() {
        IFn fn = Clojure.var (ns, "get-description");
        return (Description) fn.invoke (NAME);
    }

    // Here is  the meat  of the  plugin implementation,  which should
    // perform the appropriate logic for your plugin.
    //
    // The PluginStepContext provides access to the appropriate Nodes,
    // the configuration  of the  plugin, and  details about  the step
    // number and context.
    public void executeStep (final PluginStepContext context, final Map<String, Object> configuration)
        throws StepException {

        IFn fn = Clojure.var (ns, "execute-step");

        // This  will  throw  a   StepException  for  some  values  of
        // parameters:
        fn.invoke (context, configuration);
    }
}
