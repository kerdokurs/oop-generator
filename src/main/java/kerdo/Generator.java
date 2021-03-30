package kerdo;

import kerdo.g.Argument;
import kerdo.g.Extendable;
import kerdo.g.Interface;
import kerdo.g.Method;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Generator {
  public static final String COMMENT = "/**\n" +
    " * Klass: $\n" +
    " * Autor: Kerdo Kurs\n" +
    " * \n" +
    " * Genereeritud, kasutades oop-generatorit\n" +
    " * https://github.com/kerdokurs/oop-generator\n" +
    " */\n";

  private final Logger logger = Logger.getLogger(getClass().getSimpleName());

  private final Map<String, String> templates = new HashMap<>();
  private final List<String> sections = new ArrayList<>();

  private final Map<String, Interface> interfaces = new HashMap<>();
  private final Map<String, GClass> classes = new HashMap<>();

  public static void main(final String[] args) {
    final Generator generator = new Generator();
    generator.generate();
  }

  public void generate() {
    loadTemplates();
    loadSource();
    parseSections();
    writeClasses();
  }

  private void writeClasses() {
//    final GClass gClass = new GClass("Teos", true, null, null);
//    final Method gMethod = new Method("test", "void");
//    gMethod.addArgument(new Argument("String", "name"));
//    gMethod.addArgument(new Argument("int", "num"));
//    gClass.addMethod(gMethod);
//    try (final FileOutputStream fos = new FileOutputStream("out/Teos.java")) {
//      fos.write(gClass.toString().getBytes());
//    } catch (final IOException e) {
//      e.printStackTrace();
//    }
    interfaces.forEach((className, inte) -> writeToFile(className, inte.toString()));
    classes.forEach((className, gClass) -> writeToFile(className, gClass.toString()));
  }

  private void writeToFile(final String className, final String data) {
    try (final FileOutputStream fos = new FileOutputStream("out/" + className + ".java")) {
      fos.write(data.getBytes());
    } catch (IOException e) {
      logger.warning(String.format("an unknown error has occurred while trying to write a class to disk: %s", e));
    }
  }

  private void parseSections() {
    logger.info("parsing sections");

    final Pattern classNamePattern = Pattern.compile("^(Peaklass|(Abstraktsel )?(([Kk]lass(il)?)|Liides) (\\w+))");
    final Pattern implementsPattern = Pattern.compile("([Kk]lass (\\w+))? ?realiseerib liidest (\\w+)");
    final Pattern extendsPattern = Pattern.compile("(\\w+) alamklass");
    final Pattern methodPattern = Pattern.compile("(abstraktne )?((\\w+)-tüüpi )?( ?\\w+ ?)?[Mm]eetod(it)? (\\w+),? ([\\w õäöü,\\(\\)]*)\\.");
    final Pattern argumentPattern = Pattern.compile("(\\w+) \\((\\w+)\\)");

    for (final String section : sections) {
      Matcher m = classNamePattern.matcher(section);

      boolean isAbstract = false;
      String classType = "";
      String className = "";
      String implement = "";
      String extend = "";

      // TODO: Implement this.
      String varNames = "";

      if (m.find()) {
//        System.out.printf("%s, %s, %s%n", m.group(1), m.group(2), m.group(4));
//        System.out.println(m.group());
//        final String classType = m.group(1).equals("Klass") ? "class" : "interface";
        isAbstract = m.group(1).toLowerCase().contains("abstrakt");
        classType = m.group(3) != null ? (m.group(3).toLowerCase().contains("klass") ? "class" : "interface") : "";
//        System.out.println(m.group());
        className = m.group(6) != null ? m.group(6) : m.group();
        System.out.println("className = " + className);
//        System.out.printf("abs=%s,type=%s,name=%s%n", isAbstract, classType, className);

      }

      if ("".equals(className)) continue;

      m = implementsPattern.matcher(section);

      if (m.find()) {
        implement = m.group(2);
      }

      m = extendsPattern.matcher(section);

      if (m.find()) {
        extend = m.group(1);
      }

      System.out.println("extend = " + extend);

      m = methodPattern.matcher(section);

      final List<Method> methods = new ArrayList<>();

      while (m.find()) {
        final String returnType = m.group(3) != null ? m.group(3) : "void";
        final String methodName = m.group(6);

        if (methodName == null) continue;

        final String argumentsSpec = m.group(7);

        final Matcher am = argumentPattern.matcher(argumentsSpec);

        Method method = new Method(methodName, returnType);

        while (am.find()) {
          final String argumentType = am.group(2);
          final String argumentName = am.group(1);

          method.addArgument(new Argument(argumentType, argumentName));
        }

        methods.add(method);
      }

      if ("interface".equals(classType)) {
        final Interface intr = new Interface(className);

        intr.getMethods().addAll(methods);

        interfaces.put(className, intr);
      } else {
        final Extendable exte = classes.get(extend);
        Interface impl = interfaces.get(implement);

        final GClass gClass = new GClass(className, isAbstract, exte, impl);

        gClass.getMethods().addAll(methods);

        classes.put(className, gClass);
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
