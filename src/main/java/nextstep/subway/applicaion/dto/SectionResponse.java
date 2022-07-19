package nextstep.subway.applicaion.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SectionResponse {

    private Long id;

    private Long lineId;

    private Long upStationId;

    private Long downStationId;

    private Long distance;
}