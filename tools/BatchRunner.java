package track;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

/**
 * Dev-only batch runner: feed every {@code *.in} in a directory through {@link App#run} and write
 * {@code *.got} beside it, in a single JVM.
 *
 * <p>Usage: {@code javac -d <out> src/main/java/track/App.java tools/BatchRunner.java} then
 * {@code java -cp <out> track.BatchRunner <inDir> <outDir>}
 */
public final class BatchRunner {
  private BatchRunner() {
  }

  public static void main(String[] args) throws IOException {
    Path inDir = Path.of(args[0]);
    Path outDir = Path.of(args[1]);
    Files.createDirectories(outDir);
    try (Stream<Path> stream = Files.list(inDir)) {
      List<Path> ins = stream
          .filter(p -> p.getFileName().toString().endsWith(".in"))
          .sorted()
          .toList();
      for (Path in : ins) {
        String fileName = in.getFileName().toString();
        String base = fileName.substring(0, fileName.length() - 3);
        try (BufferedReader br = Files.newBufferedReader(in, StandardCharsets.UTF_8)) {
          String out = App.run(br);
          Files.writeString(outDir.resolve(base + ".got"), out, StandardCharsets.UTF_8);
        }
      }
    }
  }
}
