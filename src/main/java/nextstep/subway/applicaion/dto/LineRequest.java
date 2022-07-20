package nextstep.subway.applicaion.dto;

import lombok.Getter;
import nextstep.subway.domain.Color;
import nextstep.subway.domain.Line;

@Getter
public class LineRequest {

  private String name;

  private Color color;

  private Long upStationId;

  private Long downStationId;

  private int distance;

  public LineRequest() {}

  public LineRequest(String name, Color color, Long upStationId, Long downStationId, int distance) {
    this.name = name;
    this.color = color;
    this.upStationId = upStationId;
    this.downStationId = downStationId;
    this.distance = distance;
  }

  public static Line createLine(LineRequest request) {
    return Line.builder()
        .name(request.getName())
        .color(request.getColor())
        .build();
  }
}
