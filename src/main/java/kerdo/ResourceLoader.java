package kerdo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ResourceLoader {
  public static String load(final String path) {
    if (path == null || path.isEmpty()) return null;

    try (
        final InputStream is = Generator.class.getClassLoader().getResourceAsStream(path);
        final BufferedReader br = new BufferedReader(new InputStreamReader(is))
    ) {
      final StringBuilder sb = new StringBuilder();
      String line;
      while ((line = br.readLine()) != null)
        sb.append(line).append("\n");

      return sb.toString();
    } catch (final IOException e) {
      System.err.printf("mingi error: %s%n", e);
    }

    return null;
  }
}
