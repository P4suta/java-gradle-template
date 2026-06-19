package track;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.UncheckedIOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/** Runs every {@code resources/samples/*.in} through {@link App#run} and checks the {@code .out}. */
class SampleIoTest {

  @ParameterizedTest(name = "{0}")
  @MethodSource("sampleNames")
  void matchesExpectedOutput(String name) throws IOException {
    String input = resource("/samples/" + name + ".in");
    String expected = resource("/samples/" + name + ".out");
    String actual = App.run(new BufferedReader(new StringReader(input)));
    assertEquals(expected, actual, () -> "mismatch for " + name);
  }

  static List<String> sampleNames() {
    var url = SampleIoTest.class.getResource("/samples");
    if (url == null) {
      throw new IllegalStateException("samples directory not found on classpath");
    }
    try (Stream<Path> files = Files.list(Path.of(url.toURI()))) {
      List<String> names = new ArrayList<>();
      files.map(p -> p.getFileName().toString())
          .filter(f -> f.endsWith(".in"))
          .sorted()
          .forEach(f -> names.add(f.substring(0, f.length() - ".in".length())));
      return names;
    } catch (IOException | URISyntaxException e) {
      throw new IllegalStateException("failed to list samples", e);
    }
  }

  private static String resource(String path) {
    try (var in = SampleIoTest.class.getResourceAsStream(path)) {
      if (in == null) {
        throw new IllegalStateException("resource not found: " + path);
      }
      return new String(in.readAllBytes(), StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
