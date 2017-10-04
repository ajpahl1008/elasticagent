# elasticagent

The purpose of this project is to provide an easy mechanism for sending your application performance data into apm-server by Elastic without having to modify your source code.

# Compile
gradle clean build

# Run
java -javaagent:elasticagent-1.0-SNAPSHOT.jar -jar AppYouWantToMeasure.jar

# Warnings
The IP Address is currently hard coded in APMMessageFactory.
Config files are the next big feature update.

