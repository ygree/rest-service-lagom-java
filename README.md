Rest Service with Lagom Framework and Java
------------------------------------------

This is an example application that makes a use of:

- Lagom with Java
- Jdbi with Lagom JdbcSession running by Slick behind the scene and providing virtually async JDBC interface

Run Locally
-----------

`sbt runAll`

Simple GET request
------------------

`curl -i http://localhost:61450/api/hello/world`
`curl -i -u test:test http://localhost:61450/api/auth-hello/world`

Multiple GET params
-------------------

```
curl -i http://localhost:61450/api/data/v1/indexlevels/indexKey/20180101/20180228?frequency=DAILY&includeChildren=false
```


Build and run docker image
--------------------------

`sbt restsrv-impl/docker:publishLocal`

`docker run -p 9000:9000 restsrv-impl:1.0-SNAPSHOT`

Build zip package
-----------------

`sbt universal:packageBin`

it will generate zip archive in: `restsrv-impl/target/universal/`
It will contain bin directory with scripts to run this application.

