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

// Resource   Model  Plugin   is   a  factory   that   can  create   a
// ResourceModelSource based on a configuration.
@Plugin(name = "HelloNodes", service = "ResourceModelSource")
public class HelloNodes implements ResourceModelSourceFactory, Describable {
    public ResourceModelSource createResourceModelSource (final Properties properties)
        throws ConfigurationException {
        // FIXME: actual impl?
        return null;
    }

    public Description getDescription () {
        // FIXME: actual impl?
        return null;
    }
}
