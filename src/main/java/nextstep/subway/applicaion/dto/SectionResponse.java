package nextstep.subway.applicaion.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class SectionResponse {

    private Long upStationId;
    private Long downStationId;
    private Long distance;

    public SectionResponse(Long upStationId, Long downStationId, Long distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

}
