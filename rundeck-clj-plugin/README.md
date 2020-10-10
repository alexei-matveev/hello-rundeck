# rundeck-clj-plugin

Trying Clojure for Rundeck plugin.

## Installation

Build it:

    $ lein uberjar

Install it into libext:

    $ kubectl cp target/uberjar/rundeck-clj-plugin-0.1.0.jar hello-rundeck/rundeck-server-xxx:/home/rundeck/libext/
    $ kubectl exec rundeck-server-xxx -- ls -l /home/rundeck/libext

Or upload the JAR file in GUI.

## License

Copyright © 2020 <alexei.matveev@gmail.com>

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
