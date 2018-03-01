# elasticagent

The purpose of this project was to document a research spike for the instrumentation API and Java Agent capapbilities.
Additionally, the goal is to provide an easy mechanism for sending your application performance data into apm-server by Elastic without having to modify your source code.

WARNING: WHILE FUNCTIONAL, THIS IS NOT PRODUCTION QUALITY CODE!!!!

# Download
```
git clone http://github.com/ajpahl1008/elasticagent.git
```

# Compile
```
gradle clean build
```

# Run
```
java -javaagent:elasticagent-1.0-SNAPSHOT.jar -Dpath.config=/path/to/config.yml -jar AppYouWantToMeasure.jar
```
