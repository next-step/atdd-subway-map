package subway.application.dto;

import lombok.Getter;
import subway.domain.Line;

@Getter
public class LineCreateRequest {

  private String name;
  private String color;
  private Long upStationId;
  private Long downStationId;
  private Integer distance;

  public Line to() {
    return Line.builder()
        .name(this.getName())
        .color(this.getColor())
        .upStationId(this.getUpStationId())
        .downStationId(this.getDownStationId())
        .distance(this.getDistance())
        .build();
  }
}
