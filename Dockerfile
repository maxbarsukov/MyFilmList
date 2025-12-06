FROM gradle:7.6-jdk17 AS builder
WORKDIR /app
COPY . .
ENV VENDOR_PATH=vendor
RUN ./gradlew clean build -x test --no-daemon

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

RUN apt-get update && apt-get install -y \
    imagemagick \
    libmagickwand-6.q16-6 \
    libmagickcore-6.q16-6 \
    && rm -rf /var/lib/apt/lists/*

COPY --from=builder /app/build/libs/*.jar app.jar
COPY --from=builder /app/vendor /app/vendor

ENV VENDOR_PATH=/app/vendor
ENV LD_LIBRARY_PATH=/app/vendor

EXPOSE 8080

ENTRYPOINT ["java", "-Djava.library.path=/app/vendor", "-jar", "app.jar"]
