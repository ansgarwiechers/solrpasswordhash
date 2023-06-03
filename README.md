What's this about?
==================
If you want [basic authentication enabled][1] in Solr Cloud the developers expect
you to launch the service with well-known credentials (solr:SolrRocks), and then
change those credentials via the API. No, seriously.

![HAHAHAHAHAHAHAHAHAHAHAHAHAHAHAHAHAHAHA!][2]

Of course that's not what a reasonable person would want. However, the creation
of the values for setting the initial credentials is not very well documented.
So I dug the routine out of the Solr source code and wrapped it in a standalone
application.

Prerequisites
=============
A JDK and Ant.

Building
========
To compile the code and build the jar file clone the repository, `cd` into the
repository root directory and run `ant`. The build process will automatically
download the codec library required for the base64 encoding (meaning you need
Internet access at build time too).

Note: Since the Apache guys decided to remove the symlink to the current version
of the codec library (presumably in an attempt to force everyone to be more
deliberate about what library versions are included in their software) the build
file now sets a default version (currently 1.15), which you can override on the
commandline if need be:

    ant                         # build with default codec version (1.15)
    ant -Dcodecs.version=1.13   # build with codec version 1.13

If you don't want to compile the code yourself you can download the jar file
from the Releases section.

Usage
=====
To generate a password hash run

    java -jar SolrPasswordHash.jar SomePassword

The output consists of the password hash and a random salt used for the hash
calculation (both base64-encoded).

Put the two values in a file `security.json` with the following content (replace
`B64_PW_HASH` with the first value, and `B64_SALT` with the second value, both
values separated by a single space):

    {
      "authentication": {
        "blockUnknown": true,
        "class": "solr.BasicAuthPlugin",
        "credentials": {
          "solr": "B64_PW_HASH B64_SALT"
        }
      },
      "authorization": {
        "class": "solr.RuleBasedAuthorizationPlugin",
        "permissions": [
          {
            "name": "security-edit",
            "role": "admin"
          }
        ],
        "user-role": {
          "solr": "admin"
        }
      }
    }

Then load that file into Solr (or rather, into Zookeeper)

    bin/solr zk cp file:/path/to/security.json zk:/security.json -z localhost:9983

[1]: https://lucene.apache.org/solr/guide/basic-authentication-plugin.html
[2]: /img/el-risitas.png
