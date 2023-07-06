package subway.line;

import lombok.Builder;

@Builder
public class SubwayLineEditRequest {

  private String name;
  private String color;
  private Long upStationId;
  private Long downStationId;
  private Integer distance;

  public SubwayLineEditRequest() {}

  public SubwayLineEditRequest(String name, String color, Long upStationId, Long downStationId, Integer distance) {
    this.name = name;
    this.color = color;
    this.upStationId = upStationId;
    this.downStationId = downStationId;
    this.distance = distance;
  }

  public String getName() {
    return name;
  }

  public String getColor() {
    return color;
  }

  public Long getUpStationId() {
    return upStationId;
  }

  public Long getDownStationId() {
    return downStationId;
  }

  public Integer getDistance() {
    return distance;
  }
}
