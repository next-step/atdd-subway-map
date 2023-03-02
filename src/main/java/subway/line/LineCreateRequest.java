package subway.line;

public class LineCreateRequest {

  private final String name;
  private final String color;
  private final Long upStationId;
  private final Long downStationId;
  private final Long distance;

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
