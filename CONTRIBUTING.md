# Contributing

## Setup

```sh
mise trust && mise install
just setup
```

JDK 21 on `PATH` works without mise; with no JDK, use `USE_DOCKER=1 just build`.

The toolchain is always JDK 21; to target a different Java version for `src/main`, use
`just release <N>` (or `-PjavaRelease=<N>`, range 8..21).

## Workflow

1. Branch.
2. Implement; keep `just build` and `just lint` green (the pre-commit hook runs them).
3. Open a PR; CI (build / test / lint / CodeQL) must be green.

## Conventions

- Keep the quality gates green (Error Prone + NullAway / Checkstyle / SpotBugs / ArchUnit / JaCoCo).
- Output strings must match exactly; typos and the differential test guard this.
