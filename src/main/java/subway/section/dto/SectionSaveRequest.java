package subway.section.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SectionSaveRequest {

    private Long upStationId;
    private Long downStationId;
    private int distance;

    @Builder
    private SectionSaveRequest(Long upStationId, Long downStationId, int distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

}
