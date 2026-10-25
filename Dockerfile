# syntax=docker/dockerfile:1
# Minimal runtime image for the App: build the jar, then run it on a distroless JRE.

# ---- build: produce the runnable jar ----
FROM gradle:9.8.2-jdk21@sha256:37110d25b7a0f866d07f7e61968347b83d2e0fd01bd6ddf74c9ae2ff04000691 AS build
WORKDIR /app
COPY . .
RUN gradle --no-daemon --console=plain clean jar

# ---- runtime: distroless JRE ----
FROM gcr.io/distroless/java21-debian12:nonroot@sha256:7e37784d94dccbf5ccb195c73b295f5ad00cd266512dfbac12eb9c3c28f8077d AS runtime
WORKDIR /app
COPY --from=build /app/build/libs/*.jar /app/app.jar
# Pipe input on stdin:  docker run -i --rm <image> < input.txt
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
