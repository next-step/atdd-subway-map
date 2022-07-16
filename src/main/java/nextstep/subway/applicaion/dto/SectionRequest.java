package nextstep.subway.applicaion.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.domain.Section;

@Getter
@NoArgsConstructor
public class SectionRequest {

    private Long downStationId;
    private Long upStationId;
    private Integer distance;

    @Builder
    public SectionRequest(Long upStationId, Long downStationId, Integer distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

}
