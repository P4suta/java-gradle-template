package track;

import java.util.List;

/**
 * Independent reference implementation (test-only) for the differential test. Implement it
 * separately from {@link App} so both don't share the same mistake. This stub echoes the input.
 */
final class ReferenceSystem {

  private ReferenceSystem() {
  }

  /** Processes all input lines and returns the response (trailing newline). */
  static String runAll(List<String> lines) {
    var out = new StringBuilder();
    for (String line : lines) {
      out.append(line).append('\n');
    }
    return out.toString();
  }
}
