package subway.application.dto;

import lombok.Getter;
import subway.domain.Line;

@Getter
public class LineCreateRequest {

  private Long upStationId;
  private Long downStationId;
  private String name;
  private String color;
  private int distance;

  public Line to() {
    return new Line(name, color);
  }
}
