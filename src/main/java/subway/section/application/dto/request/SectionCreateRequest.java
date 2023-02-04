package subway.section.application.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SectionCreateRequest {
    private final Long upStationId;
    private final Long downStationId;
    private final Long distance;

    @Builder
    private SectionCreateRequest(final Long upStationId, final Long downStationId, final Long distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }
}
