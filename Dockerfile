# syntax=docker/dockerfile:1
# Minimal runtime image for the App: build the jar, then run it on a distroless JRE.

# ---- build: produce the runnable jar ----
FROM gradle:9.5.1-jdk21 AS build
WORKDIR /app
COPY . .
RUN gradle --no-daemon --console=plain clean jar

# ---- runtime: distroless JRE ----
FROM gcr.io/distroless/java21-debian12:nonroot AS runtime
WORKDIR /app
COPY --from=build /app/build/libs/*.jar /app/app.jar
# Pipe input on stdin:  docker run -i --rm <image> < input.txt
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
