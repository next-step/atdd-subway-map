package nextstep.subway.applicaion.line.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LineResponse {
    private final Long id;
    private final String name;
    private final String color;
    private final Long upStationId;
    private final Long downStationId;
    private final Integer distance;
}
