package subway.section;

public class SectionRequest {
    private Long lineId;
    private Long upStationId;
    private Long downStationId;

    public SectionRequest() {
    }

    public SectionRequest(Long lineId, Long upStationId, Long downStationId) {
        this.lineId = lineId;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
    }

    public Long getLineId() {
        return lineId;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }
}
