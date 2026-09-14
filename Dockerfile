FROM ubuntu/jdk:21-24.04
LABEL authors="ltsp"
ADD target/bookdesign-0.0.1-SNAPSHOT.jar bookdesign-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "bookdesign-0.0.1-SNAPSHOT.jar"]