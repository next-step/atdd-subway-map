package subway.line;

public class LinePatchRequest {
  private String name;
  private String color;

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
