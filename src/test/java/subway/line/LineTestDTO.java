package subway.line;

public class LineTestDTO {
  public static class LineStationCreateDTO {
    private final String name;
    private final String color;
    private final String upStation;
    private final String downStation;
    private final Long distance;

    public LineStationCreateDTO(String name, String color, String upStation, String downStation,
        Long distance) {
      this.name = name;
      this.color = color;
      this.upStation = upStation;
      this.downStation = downStation;
      this.distance = distance;
    }

    public String getName() {
      return name;
    }

    public String getColor() {
      return color;
    }

    public String getUpStation() {
      return upStation;
    }

    public String getDownStation() {
      return downStation;
    }

    public Long getDistance() {
      return distance;
    }
  }


}
