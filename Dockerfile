# syntax=docker/dockerfile:1
# Minimal runtime image for the App: build the jar, then run it on a distroless JRE.

# ---- build: produce the runnable jar ----
FROM gradle:9.9.1-jdk21@sha256:607055593f1acc874a98aa7c1cb5adb414b3879bb0a5e1e7240180020fb97277 AS build
WORKDIR /app
COPY . .
RUN gradle --no-daemon --console=plain clean jar

# ---- runtime: distroless JRE ----
FROM gcr.io/distroless/java21-debian12:nonroot@sha256:7e37784d94dccbf5ccb195c73b295f5ad00cd266512dfbac12eb9c3c28f8077d AS runtime
WORKDIR /app
COPY --from=build /app/build/libs/*.jar /app/app.jar
# Pipe input on stdin:  docker run -i --rm <image> < input.txt
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
