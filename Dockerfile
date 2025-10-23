# Utiliser Tomcat 9 avec JDK 17
FROM tomcat:9.0.84-jdk17-temurin

# Nettoyage des applications par défaut
RUN rm -rf /usr/local/tomcat/webapps/*

# Copier le WAR dans le dossier webapps de Tomcat
COPY target/spring-user-mgt.war /usr/local/tomcat/webapps/ROOT.war

# Exposer le port HTTP par défaut
EXPOSE 8080

# Démarrage de Tomcat
CMD ["catalina.sh", "run"]
