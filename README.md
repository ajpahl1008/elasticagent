# elasticagent

The purpose of this project is to provide an easy mechanism for sending your application performance data into apm-server by Elastic without having to modify your source code.

# Compile
gradle clean build

# Run
java -javaagent:elasticagent-1.0-SNAPSHOT.jar -Dpath.config=/path/to/config.yml -jar AppYouWantToMeasure.jar

# Roadmap
1.) Finish testing this on Websphere Liberty and provide some instructions on setup of server.xml and jvm.options
2.) Maybe JBOSS?
