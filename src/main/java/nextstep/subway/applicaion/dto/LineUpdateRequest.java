package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Color;

public class LineUpdateRequest {

  private String name;

  private Color color;

  public String getName() {
    return name;
  }

  public Color getColor() {
    return color;
  }
}
