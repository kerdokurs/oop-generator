package kerdo;

public class ClassGenerator {
  public static String generate(
      String source,
      final boolean isAbstract,
      final String classType,
      final String className,
      final String extend,
      final String implement,
      final String varNames,
      final String constructor,
      final String methods
  ) {
    return source
        .replace("{$abstract}", isAbstract ? "abstract" : "")
        .replace("{$class}", classType)
        .replace("{$className}", className)
        .replace("{$extends}", extend)
        .replace("{$implements}", implement)
        .replace("{$varNames}", varNames)
        .replace("{$constructor}", constructor)
        .replace("{$methods}", methods);
  }
}
