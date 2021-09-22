FROM openjdk:11

# Default to UTF-8 file.encoding
ENV LANG C.UTF-8

RUN mkdir -p /opt
ADD maven/opt/table-spring.jar /opt/table-spring.jar


EXPOSE 8080

ENTRYPOINT java -jar /opt/table-spring.jar -Djava.net.preferIPv4Stack=true
