package nextstep.subway.line;

public enum LineColor {

  RED("RED"),YELLOW("YELLOW"),GREEN("GREEN");

  private String color;

  LineColor(String color) { }

  public static String of(LineColor lineColor) {
    return lineColor.toString();
  }

}
