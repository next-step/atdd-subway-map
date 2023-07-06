package subway.line;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;


@Builder
public class SubwayLineRequest {

  private String name;
  private String color;
  private Long upStationId;
  private Long downStationId;
  private int distance;

  public SubwayLineRequest() {}

  public SubwayLineRequest(String name, String color, Long upStationId, Long downStationId, int distance) {
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

  public int getDistance() {
    return distance;
  }

  @JsonIgnore
  public SubwayLine toEntity() {
    return SubwayLine.builder()
        .color(this.color)
        .name(this.name)
        .upStationId(this.upStationId)
        .downStationId(this.downStationId)
        .distance(this.distance)
        .build();
  }
}
