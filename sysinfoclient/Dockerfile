FROM amazoncorretto:17-alpine-jdk

# create a directory for the app
WORKDIR /sysinfoclient

#Copy everything from the current directory to the app directory
COPY . .

# Build the app
RUN ./gradlew clean build -x test

# Run the app
CMD ["java", "-jar", "./build/libs/sysinfoclient-0.0.1-SNAPSHOT.jar"]

RUN keytool -import -alias myalias -keystore $JAVA_HOME/lib/security/cacerts -file /sysinfoclient/src/main/resources/certs/mycert.cer -storepass changeit -noprompt