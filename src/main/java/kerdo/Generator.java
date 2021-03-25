package kerdo;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Generator {
  private final Logger logger = Logger.getLogger(getClass().getSimpleName());

  private final Map<String, String> templates = new HashMap<>();
  private final List<String> sections = new ArrayList<>();

  private final Map<String, String> classes = new HashMap<>();

  public static void main(final String[] args) {
    final Generator generator = new Generator();
    generator.generate();
  }

  public void generate() {
    loadTemplates();
    loadSource();
    parseSections();
  }

  private void parseSections() {
    logger.info("parsing sections");

    final Pattern classNamePattern = Pattern.compile("^(Peaklass|(Abstraktsel )?(([Kk]lass(il)?)|Liides) (\\w+))");
    final Pattern implementsPattern = Pattern.compile("([Kk]lass (\\w+))? ?realiseerib liidest (\\w+)");

    for (final String section : sections) {
      Matcher m = classNamePattern.matcher(section);

      boolean isAbstract = false;
      String classType = "";
      String className = "";
      String implement = "";

      if (m.find()) {
//        System.out.printf("%s, %s, %s%n", m.group(1), m.group(2), m.group(4));
//        System.out.println(m.group());
//        final String classType = m.group(1).equals("Klass") ? "class" : "interface";
        isAbstract = m.group(1).toLowerCase().contains("abstrakt");
        classType = m.group(3) != null ? (m.group(3).toLowerCase().contains("klass") ? "class" : "interface") : "";
//        System.out.println(m.group());
        className = m.group(6) != null ? m.group(6) : "class " + m.group();
//        System.out.printf("abs=%s,type=%s,name=%s%n", isAbstract, classType, className);

      }

      if ("".equals(className)) continue;

      m = implementsPattern.matcher(section);

      if (m.find()) {
        implement = "implements " + m.group(2);
      }

      final var aClass = ClassGenerator.generate(
        templates.get("class"),
        isAbstract,
        classType,
        className,
        "", implement, "", ""
      );
      System.out.println(aClass);

      try {
        final var s = className.split(" ");
        try (FileOutputStream fos = new FileOutputStream("out/" + s[s.length - 1] + ".java")) {
          fos.write(aClass.getBytes());
        }
      } catch (final IOException e) {
        e.printStackTrace();
      }
    }

    logger.info("parsing sections finished");
  }

  private void loadSource() {
    logger.info("loading source");

    sections.addAll(
      Arrays.stream(
        ResourceLoader.load("source.txt")
          .split("\\d\\.")
      )
        .map(String::trim)
        .collect(Collectors.toList())
    );

//    final StringBuilder section = new StringBuilder();
//    System.out.println(source.length);
//    for (int i = 0; i < source.length; i++) {
//      final String rida = source[i];
//      final Matcher m = newSectionPattern.matcher(rida);
//
//      if (m.find() && section.length() > 0 || i == sourc<e.length - 1) {
//        System.out.println(i);
//        sections.add(section.toString());
//        section.setLength(0);
//      }
//
//      section.append(rida);
//    }

    logger.info(String.format("found %d sections", sections.size()));
  }

  private void loadTemplates() {
    logger.info("loading templates");

    final String templatesToLoad = ResourceLoader.load("templates.txt");
    Arrays.stream(templatesToLoad.split("\n")).forEach(rida -> {
      templates.put(
        rida,
        ResourceLoader.load(String.format("templates/%s-template.txt", rida))
      );

      logger.info(String.format("loaded template: %s", rida));
    });

    logger.info(String.format("loaded %d templates", templates.size()));
  }
}
