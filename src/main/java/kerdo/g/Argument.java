package kerdo.g;

import lombok.Getter;

public class Argument {
  @Getter
  private final String type, name;

  public Argument(final String type, final String name) {
    this.type = type;
    this.name = name;
  }

  @Override
  public String toString() {
    return String.format("final %s %s", type, name);
  }
}
