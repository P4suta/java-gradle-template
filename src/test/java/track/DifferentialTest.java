package track;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.UncheckedIOException;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Differential test: compare {@link App} against the independent {@link ReferenceSystem} over many
 * generated inputs. Set the count with {@code -Dsweep.count=<N>} (default 2000).
 */
class DifferentialTest {

  @Test
  void differentialSweep() {
    int total = Integer.getInteger("sweep.count", 2000);
    int failures = 0;
    var report = new StringBuilder();
    for (int seed = 0; seed < total; seed++) {
      QueryGenerator.Generated g = new QueryGenerator(seed).generate();
      String actual = runApp(g.lines());
      String expected = g.expected();
      if (!actual.equals(expected)) {
        failures++;
        if (failures <= 5) {
          report.append(describeMismatch(seed, g.lines(), expected, actual));
        }
      }
    }
    if (failures > 0) {
      fail("differential failures=" + failures + report);
    }
  }

  private static String runApp(List<String> lines) {
    String input = String.join("\n", lines) + "\n";
    try {
      return App.run(new BufferedReader(new StringReader(input)));
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private static String describeMismatch(int seed, List<String> lines, String expected, String actual) {
    return String.format(
        "%n[MISMATCH] seed=%d%n--- input ---%n%s%n--- expected(ref) ---%n%s--- actual(App) ---%n%s",
        seed, String.join("\n", lines), expected, actual);
  }
}
