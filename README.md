# Coding-training template

A template for stdin → stdout coding challenges (algorithm / simulation). The scaffolding —
build, quality gates, differential test, dev tooling — is fixed; per challenge you write only the
solution. The initial stub echoes input, so a fresh clone is green immediately.

## Quick start

```sh
mise trust && mise install   # one-time: JDK 21 + just/lefthook/typos/... (downloads once)
just setup                   # install git hooks, warm the Gradle + dependency caches
just dev                     # fast loop: re-run tests on change
```

`mise install` + `just setup` download the JDK, Gradle, and dependencies once (a few hundred MB);
after that, builds are incremental (seconds). No Docker required — append `USE_DOCKER=1` to any
recipe to run inside `gradle:8.14.1-jdk21` instead.

## Start a new challenge

1. Create a repo from this template ("Use this template") and clone it.
2. (Optional) rename the `track` package — see the checklist below. It works as-is.
3. Implement:
   - `src/main/java/track/App.java` — `run(BufferedReader)`, the submitted solution.
   - `src/test/java/track/ReferenceSystem.java` — an independent reference.
   - `src/test/java/track/QueryGenerator.java` — input generator (bias toward boundaries).
   - `src/test/resources/samples/` — `NN_name.in` / `NN_name.out` pairs (auto-discovered).
4. Run `just build` until green.

## Target Java version (per submission)

Judges differ in Java version, so the solution's compile target is a one-knob switch:

```sh
just release 8                 # compile src/main against Java 8 (range 8..21; default 21)
./gradlew build -PjavaRelease=8 # or ad hoc, without persisting
```

The toolchain, tests, CI, and Docker all stay on JDK 21 — only `src/main`'s bytecode/API level
changes (via `--release`), so newer-API usage is caught at build time. CI builds every target
(8 / 11 / 17 / 21) to keep the switch honest.

## Commands

| Command | What |
|---|---|
| `just setup` | Install hooks, warm caches (one-time) |
| `just dev` | Re-run tests on change (fast loop) |
| `just build` | All quality gates + tests + coverage |
| `just release <N>` | Set the solution's Java compile target (8..21) |
| `just test` | Tests only (sweep 2000) |
| `just sweep 30000` | Differential sweep with a given count |
| `just coverage` | Coverage report (HTML) |
| `just lint` | All linters + build over the whole tree |
| `just run-sample <file>` | Pipe a sample to stdin |
| `just clean` | Remove build outputs |
| `just init` | Docker cache volume (only with `USE_DOCKER=1`) |

`./gradlew clean build` works directly too.

## Quality gates

`just build` fails if any of these fail:

| Layer | Tool | Catches |
|---|---|---|
| Compile-time analysis | Error Prone + NullAway | null dereferences, known bug patterns |
| Style | Checkstyle (0 warnings) | unused imports, missing braces, ... |
| Bytecode analysis | SpotBugs (effort MAX) | bug patterns |
| Design | ArchUnit | I/O confined to the entry point, ... |
| Differential test | JUnit5 + reference + generator | App vs reference over random inputs |
| Coverage | JaCoCo | under-tested code (fails below threshold) |

Coverage thresholds are at the top of `build.gradle` (`lineCoverageMin` / `branchCoverageMin`);
raise them per challenge after checking `just coverage`.

## Dev tooling

`mise install` provides tools wired into git hooks by lefthook: typos, taplo, yamllint,
shellcheck, editorconfig-checker, gitleaks, actionlint, hadolint.

- pre-commit: lint staged files + `./gradlew build`.
- pre-push: re-run over the whole tree.

Run it all manually with `just lint`.

## CI & images

- `.github/workflows/ci.yml` — build + test on Linux/Windows/macOS, plus the lint suite.
- `.github/workflows/codeql.yml` — CodeQL analysis.
- `.github/workflows/docker-publish.yml` — on push to `main`, publishes two GHCR images:
  `:latest` (distroless runtime) and `:toolchain` (JDK + Gradle + lint tools).

## Rename checklist (only to change `track`)

- `settings.gradle` `rootProject.name`
- `build.gradle` `group`, `application.mainClass`, `NullAway:AnnotatedPackages`
- `src/main/java/track/`, `src/test/java/track/` directories and `package track;` declarations
- `justfile` `src/main/java/track/App.java` path
- `ArchitectureTest.java` `@AnalyzeClasses(packages = "track")` and the `App` name prefix

## License

Apache-2.0. See `LICENSE`.
