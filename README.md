Rest Service with Lagom Framework and Java
------------------------------------------

TODO
----

1. Add token verification
2. Add Maven

Run Locally
-----------

`sbt runAll`

`curl -i http://localhost:61450/api/hello/world`

Build and run docker image
--------------------------

`sbt restsrv-impl/docker:publishLocal`

`docker run -p 9000:9000 restsrv-impl:1.0-SNAPSHOT`

Build zip package
-----------------

`sbt universal:packageBin`

it will generate zip archive in: `restsrv-impl/target/universal/`
It will contain bin directory with scripts to run this application.

