package kerdo.g;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Method {
  @Getter
  private final String name;

  @Getter
  private final String returnType;

  @Getter
  private final List<Argument> arguments = new ArrayList<>();

  public Method(final String name, final String returnType) {
    this.name = name;
    this.returnType = returnType;
  }

  @Override
  public String toString() {
    return String.format("public %s %s(%s) {%n}%n%n",
      returnType, name,
      arguments.stream()
        .map(Argument::toString)
        .collect(Collectors.joining(", "))
    );
  }

  public void addArgument(final Argument argument) {
    arguments.add(argument);
  }
}
