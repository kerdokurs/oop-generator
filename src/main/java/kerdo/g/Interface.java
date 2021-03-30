package kerdo.g;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Interface implements Extendable {
  @Getter
  private final String name;
  @Getter
  private final List<Method> methods = new ArrayList<>();

  public Interface(final String name) {
    this.name = name;
  }

  public void addMethod(final Method method) {
    methods.add(method);
  }

  @Override
  public String toString() {
    return "public interface " + name + " {\n" +
      methods.stream()
        .map(Method::toString)
        .collect(Collectors.joining("\n\n")) +
      "}";
  }
}
