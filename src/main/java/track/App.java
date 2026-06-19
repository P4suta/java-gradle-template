package track;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Entry point: reads stdin, writes the response to stdout.
 *
 * <p>This stub echoes the input. Replace {@link #run} with the challenge logic and keep
 * all I/O in this class so the domain logic stays pure.
 */
public final class App {

  private App() {
  }

  public static void main(String[] args) throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8), 1 << 20);
    System.out.print(run(br));
  }

  /** Reads all input and returns the response, one newline-terminated line per input line. */
  static String run(BufferedReader br) throws IOException {
    StringBuilder out = new StringBuilder();
    String line = br.readLine();
    while (line != null) {
      out.append(line).append('\n');
      line = br.readLine();
    }
    return out.toString();
  }
}
