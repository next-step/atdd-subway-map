package nextstep.subway.applicaion.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SectionRequest {

  private long downStationId;
  private long upStationId;
  private int distance;
}