package nextstep.subway.application.dto;

import lombok.Getter;

@Getter
public class SectionRequest {
    private Long downStationId;
    private Long upStationId;
    private int distance;
}
