package kerdo;

import kerdo.g.Argument;
import kerdo.g.Extendable;
import kerdo.g.Interface;
import kerdo.g.Method;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static kerdo.Generator.COMMENT;

public class GClass implements Extendable {
  @Getter
  private final String name;
  private final boolean isAbstract;
  private final Extendable extend;
  private final Interface implement;
  @Getter
  private final List<Method> methods = new ArrayList<>();
  private final List<Argument> constructorArguments = new ArrayList<>();

  public GClass(final String name, final boolean isAbstract, final Extendable extend, final Interface implement) {
    this.name = name;
    this.isAbstract = isAbstract;
    this.extend = extend;
    this.implement = implement;
  }

  @Override
  public String toString() {
    return COMMENT.replace("$", name) +
      "public " +
      (isAbstract ? "abstract " : "") +
      name +
      (extend != null ? " extends " + extend.getName() : "") +
      (implement != null ? " implements " + implement.getName() : "") +
      " {\n" +
      variablesToString() +
      constructorToString() +
      methodsToString() +
      "}\n";
  }

  private String variablesToString() {
    return "";
  }

  private String constructorToString() {
    return String.format("public %s(%s) {%n}%n%n",
      name, constructorArguments.stream()
        .map(Argument::toString)
        .collect(Collectors.joining(", "))
    );
  }

  private String methodsToString() {
    final StringBuilder methodsString = new StringBuilder();

    methodsString.append(methods.stream()
      .map(Method::toString)
      .collect(Collectors.joining("\n\n")));

    if (implement != null)
      methodsString.append(implement.getMethods().stream().map(Method::toString).collect(Collectors.joining("\n\n")));

    return methodsString.toString();
  }

  public void addMethod(final Method method) {
    methods.add(method);
  }

  public void addConstructorArgument(final Argument argument) {
    constructorArguments.add(argument);
  }
}
