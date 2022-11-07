FROM azul/zulu-openjdk-debian:17-jre-latest

ENV LANG='en_US.UTF-8'
ENV LANGUAGE='en_US:en'
ENV LC_ALL='en_US.UTF-8'

EXPOSE 8080

RUN mkdir -p "/opt/cavitos/services/documentor"

COPY "./target/documentor.jar" "/opt/cavitos/services/documentor"

RUN useradd -ms /bin/bash documentor \
    && chown -R documentor:documentor /opt/cavitos/services/documentor

USER documentor

CMD ["java", "-jar", "-Xms64m", "-Xmx128m", "/opt/cavitos/services/documentor/documentor.jar"]
