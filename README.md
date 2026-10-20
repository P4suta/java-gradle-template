# Java coding challenge template

A Java 21 and Gradle starter for programming challenges that read stdin and write stdout.
The initial solution echoes its input.
Replace it alongside the independent reference, input generator, and sample cases.

## Start

```sh
mise trust && mise install
just setup
just build
```

`just build` runs the tests, static analysis, and coverage checks.
The solution can target Java 8 through 21 while the build tools and tests use Java 21.

```sh
just release 8
just run-sample src/test/resources/samples/00_sample_00.in
```

See the [development guide](docs/development.md) for challenge setup, commands, Docker usage, and renaming the package.
See [CONTRIBUTING.md](CONTRIBUTING.md) for the contribution workflow.

Licensed under [Apache-2.0](LICENSE).
