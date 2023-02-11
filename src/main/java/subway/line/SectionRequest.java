package subway.line;

import lombok.Getter;

@Getter
public class SectionRequest {
    private final Long downStationId;
    private final Long upStationId;
    private final Integer distance;

    public SectionRequest(final Long downStationId, final Long upStationId, final Integer distance) {
        this.downStationId = downStationId;
        this.upStationId = upStationId;
        this.distance = distance;
    }
}
