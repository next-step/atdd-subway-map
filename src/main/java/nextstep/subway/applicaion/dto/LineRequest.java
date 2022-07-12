package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Color;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Station;

public class LineRequest {

  private String name;

  private Color color;

  private Long upStationId;

  private Long downStationId;

  private int distance;

  public static Line createLine(LineRequest request, Station upStation, Station downStation) {
    return new Line(request.getName(), request.getColor(), upStation, downStation, request.distance);
  }

  public String getName() {
    return name;
  }

  public Color getColor() {
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
}
