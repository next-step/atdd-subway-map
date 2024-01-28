package subway;

public class LineCreateRequest {

  private final String name;

  private final String color;

  private final Long upStationId;

  private final Long downStationId;

  private final int distance;

  public LineCreateRequest(String name, String color, Long upStationId, Long downStationId, Integer distance) {
    this.name = name;
    this.color = color;
    this.upStationId = upStationId;
    this.downStationId = downStationId;
    this.distance = distance;
  }

  public String getName() {
    return name;
  }

  public Long getUpStationId() {
    return upStationId;
  }

  public Long getDownStationId() {
    return downStationId;
  }

  public Line to() {
    return new Line(
        this.name,
        this.color,
        this.upStationId,
        this.downStationId,
        this.distance
    );
  }

}
