# Alpine based container with Java and Tomcat
FROM centos:centos7.6.1810
MAINTAINER Nextlabs

# Prepare environment
RUN mkdir -p /opt/nextlabs/logger
RUN mkdir -p /var/opt/nextlabs/logger/shared
ENV LOGGER_INSTALL_DIR /opt/nextlabs/logger
ENV LOGGER_SHARED_DIR /var/opt/nextlabs/logger/shared
ENV JAVA_HOME $LOGGER_INSTALL_DIR/java
ENV CATALINA_HOME $LOGGER_INSTALL_DIR/tomcat
ENV PATH $PATH:$JAVA_HOME/bin:$CATALINA_HOME/bin:$CATALINA_HOME/scripts
ENV LC_ALL en_US.UTF-8

# Copy requisite dependencies (JRE/Tomcat)
# These dependencies should have been already set up outside before
# docker build command is initiated.
COPY xlib/jdk-11.0.2+7-jre/ ${JAVA_HOME}/
COPY xlib/tomcat_8.5.35 ${CATALINA_HOME}/

RUN chmod +x ${CATALINA_HOME}/bin/*.sh

# Create tomcat user
#RUN addgroup -S tomcat && \
 #adduser -G tomcat -h ${CATALINA_HOME} -S  -g "Tomcat user" tomcat && \
 #chown -R tomcat:tomcat ${CATALINA_HOME} ${JAVA_HOME}  /var/opt/nextlabs/ && \
 #chmod u+w ${JAVA_HOME}/lib/security/cacerts
RUN groupadd -r tomcat && \
  useradd -g tomcat -d ${CATALINA_HOME} -s /sbin/nologin  -c "Tomcat user" tomcat && \
  chown -R tomcat:tomcat ${CATALINA_HOME} ${JAVA_HOME} && \
  chmod u+w ${JAVA_HOME}/lib/security/cacerts
RUN  chmod -R 755 /var/opt/nextlabs/

COPY docker/logger/tomcat/conf/ $CATALINA_HOME/conf/
COPY docker/logger/tomcat/bin/ $CATALINA_HOME/bin/
COPY build/libs/logger.war  ${CATALINA_HOME}/webapps
EXPOSE 8443
COPY docker/logger/entrypoint.sh /
RUN chmod 755 /entrypoint.sh

USER tomcat
ENTRYPOINT ["/entrypoint.sh"]
CMD ["run"]


