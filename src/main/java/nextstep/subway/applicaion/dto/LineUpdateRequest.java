package nextstep.subway.applicaion.dto;

import lombok.Getter;
import nextstep.subway.domain.Color;

@Getter
public class LineUpdateRequest {

  private String name;

  private Color color;

  public LineUpdateRequest() {}

  public LineUpdateRequest(String name, Color color) {
    this.name = name;
    this.color = color;
  }
}
