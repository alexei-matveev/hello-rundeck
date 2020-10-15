//
// See e.g. EC2 Nodes Plugin as an example [1].
//
// [1] https://github.com/rundeck-plugins/rundeck-ec2-nodes-plugin
//
package rundeck_clj_plugin;

import com.dtolabs.rundeck.core.plugins.Plugin;
import com.dtolabs.rundeck.core.plugins.configuration.*;
import com.dtolabs.rundeck.core.resources.ResourceModelSource;
import com.dtolabs.rundeck.core.resources.ResourceModelSourceFactory;

import java.util.Properties;

import clojure.java.api.Clojure;
import clojure.lang.IFn;

// Resource   Model  Plugin   is   a  factory   that   can  create   a
// ResourceModelSource based on a configuration.
@Plugin(name = HelloNodes.NAME, service = "ResourceModelSource")
public class HelloNodes implements ResourceModelSourceFactory, Describable {
    static final String NAME = "HelloNodes";

    // Maybe one schould separate code on the Clojure side too:
    static final String ns = "rundeck-clj-plugin.nodes";

    // Trying to  call Clojure from  here without the voodoo  with the
    // Class Loader will fail at loading clojure/core__init.class. See
    // Internets [1].
    //
    // [1] https://groups.google.com/forum/#!msg/clojure/Aa04E9aJRog/f0CXZCN1z0AJ
    public HelloNodes () {
        Thread.currentThread()
            .setContextClassLoader (this.getClass().getClassLoader());

        // Load the namespace once:
        IFn require = Clojure.var ("clojure.core", "require");
        require.invoke (Clojure.read (ns));
    }

    public ResourceModelSource createResourceModelSource (final Properties properties)
        throws ConfigurationException {
        IFn fn = Clojure.var (ns, "create-resource-model-source");
        return (ResourceModelSource) fn.invoke (properties);
    }

    public Description getDescription () {
        IFn fn = Clojure.var (ns, "get-description");
        return (Description) fn.invoke (NAME);
    }
}
