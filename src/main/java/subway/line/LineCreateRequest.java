package subway.line;

public class LineCreateRequest {

  private String name;
  private String color;
  private Long upStationId;
  private Long downStationId;
  private Long distance;

  public String getName() {
    return name;
  }

  public Long getUpStationId() {
    return upStationId;
  }

  public Long getDownStationId() {
    return downStationId;
  }

  public String getColor() {
    return color;
  }

  public Long getDistance() {
    return distance;
  }

  public LineCreateRequest(String name, String color, Long upStationId, Long downStationId, Long distance) {
    this.name = name;
    this.color = color;
    this.upStationId = upStationId;
    this.downStationId = downStationId;
    this.distance = distance;
  }
}
