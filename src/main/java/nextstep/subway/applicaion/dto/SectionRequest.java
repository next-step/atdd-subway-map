package nextstep.subway.applicaion.dto;

import lombok.Getter;

@Getter
public class SectionRequest {
    private Long downStationId;
    private Long upStationId;
    private Long distance;
}
