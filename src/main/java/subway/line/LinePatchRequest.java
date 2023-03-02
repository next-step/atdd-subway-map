package subway.line;

public class LinePatchRequest {
  private final String name;
  private final String color;

  public LinePatchRequest(String name, String color) {
    this.name = name;
    this.color = color;
  }

  public String getName() {
    return name;
  }

  public String getColor() {
    return color;
  }
}
