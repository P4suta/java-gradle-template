package track;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

/** Unit tests for {@link App}; {@code main} is exercised so it stays covered. */
class AppTest {

  @Test
  void runEchoesEachLine() throws Exception {
    String actual = App.run(new BufferedReader(new StringReader("a\nb\nc\n")));
    assertEquals("a\nb\nc\n", actual);
  }

  @Test
  void mainReadsStdinAndWritesStdout() throws Exception {
    var originalIn = System.in;
    var originalOut = System.out;
    try {
      System.setIn(new ByteArrayInputStream("hello\nworld\n".getBytes(StandardCharsets.UTF_8)));
      var captured = new ByteArrayOutputStream();
      System.setOut(new PrintStream(captured, true, StandardCharsets.UTF_8));

      App.main(new String[0]);

      assertEquals("hello\nworld\n", captured.toString(StandardCharsets.UTF_8));
    } finally {
      System.setIn(originalIn);
      System.setOut(originalOut);
    }
  }
}
