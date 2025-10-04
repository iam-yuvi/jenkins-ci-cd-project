FROM tomcat:10.1-jdk17-temurin

# Clean default apps
RUN rm -rf /usr/local/tomcat/webapps/*

# Deploy your WAR
COPY target/java-app.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080

CMD ["catalina.sh", "run"]
