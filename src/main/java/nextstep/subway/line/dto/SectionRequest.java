package nextstep.subway.line.dto;

import nextstep.subway.line.domain.Section;

public class SectionRequest {
  private Long upStationId;
  private Long downStationId;
  private int distance;

  public SectionRequest() {
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