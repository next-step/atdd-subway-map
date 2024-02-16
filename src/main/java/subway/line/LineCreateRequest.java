package subway.line;

public class LineCreateRequest {

  private String name;

  private String color;

  private Long upStationId;

  private Long downStationId;

  private int distance;

  public String getName() {
    return name;
  }

  public Long getUpStationId() {
    return upStationId;
  }

  public Long getDownStationId() {
    return downStationId;
  }

  public int getDistance() {
    return distance;
  }

  public Line to() {
    return new Line(
        this.name,
        this.color
    );
  }

}
