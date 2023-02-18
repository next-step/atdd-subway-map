package subway;

import javax.validation.constraints.Positive;

public class SectionRequest {
    @Positive
    private Long upStationId;
    @Positive
    private Long downStationId;

    public SectionRequest() {
    }

    public SectionRequest(Long upStationId, Long downStationId) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }
}
