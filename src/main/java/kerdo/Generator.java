package kerdo;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Generator {
  private final Map<String, String> templates = new HashMap<>();

  public Generator() {
    init();
  }

  public static void main(String[] args) throws IOException {
    final Generator generator = new Generator();
    generator.generate();
  }

  private void init() {
    loadTemplates();
    System.out.println(templates);
  }

  public void generate() throws IOException {
    final String source = TemplateLoader.load("templates/class-template.txt");
  }

  private void loadTemplates() {
    final String templatesToLoad = TemplateLoader.load("templates.txt");
    Arrays.stream(templatesToLoad.split("\n")).forEach(rida ->
      this.templates.put(
        rida,
        TemplateLoader.load(String.format("templates/%s-template.txt", rida))
      )
    );
  }
}
