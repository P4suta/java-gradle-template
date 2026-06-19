package track;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Deterministic input generator (test-only) for the differential test. For real challenges,
 * bias toward boundary cases; this stub emits a few random words.
 */
final class QueryGenerator {

  /** Generated input lines with the expected output from the reference. */
  record Generated(List<String> lines, String expected) {
  }

  private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";

  private final Random rng;

  QueryGenerator(long seed) {
    this.rng = new Random(seed);
  }

  Generated generate() {
    int count = 1 + rng.nextInt(8);
    List<String> lines = new ArrayList<>();
    for (int i = 0; i < count; i++) {
      lines.add(randomToken());
    }
    return new Generated(lines, ReferenceSystem.runAll(lines));
  }

  private String randomToken() {
    int len = 1 + rng.nextInt(8);
    var sb = new StringBuilder();
    for (int i = 0; i < len; i++) {
      sb.append(ALPHABET.charAt(rng.nextInt(ALPHABET.length())));
    }
    return sb.toString();
  }
}
