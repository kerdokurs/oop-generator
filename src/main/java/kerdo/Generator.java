package kerdo;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Generator {
  private final Map<String, String> templates = new HashMap<>();
  private final List<String> sections = new ArrayList<>();

  public Generator() {
    init();
  }

  public static void main(final String[] args) throws IOException {
    final Generator generator = new Generator();
    generator.generate();
  }

  public void generate() {

  }

  private void init() {
    loadTemplates();
    loadSource();
  }

  private void loadSource() {
    final Pattern newSectionPattern = Pattern.compile("\\d\\. .+");
    final String source = ResourceLoader.load("source.txt");

    StringBuilder section = new StringBuilder();
    for (final String rida : source.split("\n")) {
      final Matcher m = newSectionPattern.matcher(rida);

      if (m.find() && section.length() > 0) {
        sections.add(section.toString());
        section.setLength(0);
      }

      section.append(rida);
    }
  }

  private void loadTemplates() {
    final String templatesToLoad = ResourceLoader.load("templates.txt");
    Arrays.stream(templatesToLoad.split("\n")).forEach(rida ->
      this.templates.put(
        rida,
        ResourceLoader.load(String.format("templates/%s-template.txt", rida))
      )
    );
  }
}
