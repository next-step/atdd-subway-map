package nextstep.subway.applicaion.dto;

import lombok.Getter;

@Getter
public class SectionRequest {
    private long upStationId;
    private long downStationId;
    private long distance;
}
