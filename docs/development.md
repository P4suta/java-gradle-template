# Development guide

Use Java 21 with the committed Gradle wrapper.
The mise profile installs the development tools, and `just setup` installs Git hooks and warms build caches.

## Create a challenge

1. Create a repository from this template and clone it.
2. Implement `src/main/java/track/App.java`, whose `run` method owns the submitted solution.
3. Implement the independent reference in `src/test/java/track/ReferenceSystem.java`.
4. Update `src/test/java/track/QueryGenerator.java` to generate inputs near boundaries.
5. Add `NN_name.in` and `NN_name.out` pairs under `src/test/resources/samples/`.
6. Run `just build`.

## Java target

```sh
just release 8
./gradlew build -PjavaRelease=8
```

`just release` persists the selected target in `gradle.properties`.
The `-PjavaRelease` option applies to one command.
Both accept Java 8 through 21 and affect the solution's bytecode and API level.
The toolchain and tests stay on Java 21.
CI compiles all four supported targets and tests on Linux, Windows, and macOS.
Linux also runs the full static analysis and coverage gates.

## Commands

| Command | Purpose |
| --- | --- |
| `just dev` | Watch for changes and rerun tests with a small sweep |
| `just build` | Run the full build and quality checks |
| `just test` | Run tests with the default 2,000-case differential sweep |
| `just sweep 30000` | Run a larger differential sweep |
| `just coverage` | Write the coverage report |
| `just lint` | Run all local lint hooks |
| `just run-sample <file>` | Run the solution with a sample on stdin |
| `just clean` | Remove Gradle build outputs |

The build includes Error Prone, NullAway, Checkstyle, SpotBugs, ArchUnit, and JaCoCo.
`build.gradle` defines the line and branch coverage thresholds.
Raise them when a challenge adds behavior.
The HTML coverage report is under `build/reports/jacoco/test/html/`.

## Local containers

The optional Docker path uses the same Gradle and Java 21 image, fixed by digest, as the runtime image's build stage.
Initialize its cache before the first build:

```sh
USE_DOCKER=1 just init
USE_DOCKER=1 just build
```

`Dockerfile` builds a local runnable application image.
`Dockerfile.toolchain` builds a local image with the mise development tools.
CI does not build or publish registry images.

## Rename the package

Update `settings.gradle`, the group and main-class settings in `build.gradle`, and NullAway's annotated package.
Rename both source package directories and declarations.
Update the standalone path in `justfile` and the package and application checks in `ArchitectureTest.java`.

See [CONTRIBUTING.md](../CONTRIBUTING.md).
