# Dev tasks. Native by default (mise's JDK + Gradle wrapper). Set USE_DOCKER=1 to run the
# same recipe inside gradle:8.14.1-jdk21 (no host JDK needed).
#   just build              # native
#   USE_DOCKER=1 just build # Docker

use_docker := env_var_or_default("USE_DOCKER", "0")
uid := `id -u 2>/dev/null || echo 1000`
gid := `id -g 2>/dev/null || echo 1000`
img := "gradle:8.14.1-jdk21"

# Docker prefix, only when USE_DOCKER=1 (host user; deps cached in a named volume).
_dr := if use_docker == "1" { "docker run --rm --user " + uid + ":" + gid + ' -e GRADLE_USER_HOME=/cache -e HOME=/tmp -v "$PWD":/work -v practice-gradle:/cache -w /work ' + img + " " } else { "" }
# No daemon under Docker (throwaway container); keep it for native incremental builds.
_daemon := if use_docker == "1" { "--no-daemon" } else { "" }

default:
    @just --list

# One-time setup: install git hooks and warm the Gradle + dependency caches.
setup:
    lefthook install
    {{_dr}}./gradlew build {{_daemon}} --console=plain

# Fast inner loop: re-run tests on file change (small sweep for speed).
dev:
    {{_dr}}./gradlew test --continuous -Dsweep.count=200 {{_daemon}} --console=plain

# All quality gates + tests + coverage.
build:
    {{_dr}}./gradlew clean build {{_daemon}} --console=plain

# Tests only (differential sweep defaults to 2000).
test:
    {{_dr}}./gradlew test {{_daemon}} --console=plain

# Differential sweep with a given count.
sweep count="30000":
    {{_dr}}./gradlew clean test -Dsweep.count={{count}} {{_daemon}} --console=plain

# Coverage report (HTML).
coverage:
    {{_dr}}./gradlew test jacocoTestReport {{_daemon}} --console=plain
    @echo "report: build/reports/jacoco/test/html/index.html"

# All linters + build gate over the whole tree.
lint:
    lefthook run pre-commit --all-files

# Check that App.java compiles standalone (JDK only).
check-standalone:
    {{_dr}}bash -c 'rm -rf /tmp/o && mkdir -p /tmp/o && javac -d /tmp/o src/main/java/track/App.java && echo OK'

# Run a sample file through stdin.
run-sample file="src/test/resources/samples/00_sample_00.in":
    {{_dr}}./gradlew runSample -PsampleIn={{file}} {{_daemon}} --console=plain -q

# Docker dependency-cache volume (only needed with USE_DOCKER=1).
init:
    docker volume create practice-gradle >/dev/null
    docker run --rm -v practice-gradle:/cache {{img}} chown -R {{uid}}:{{gid}} /cache
    @echo "cache ready"

# Remove build outputs.
clean:
    {{_dr}}./gradlew clean {{_daemon}} --console=plain
