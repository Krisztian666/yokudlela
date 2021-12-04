FROM openjdk:11

# Default to UTF-8 file.encoding
ENV LANG C.UTF-8

RUN mkdir -p /opt
ADD maven/opt/media-spring.jar /opt/media-spring.jar


EXPOSE 8080

ENTRYPOINT java -jar /opt/media-spring.jar -Djava.net.preferIPv4Stack=true
