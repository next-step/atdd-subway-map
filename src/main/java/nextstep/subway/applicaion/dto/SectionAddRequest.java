package nextstep.subway.applicaion.dto;

public class SectionAddRequest {
    private Long lineId;
    private Long downStationId;
    private Long upStationId;
    private int distance;

    public SectionAddRequest() {
    }

    public SectionAddRequest(final Long lineId) {
        this.lineId = lineId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public int getDistance() {
        return distance;
    }

    public void setLineId(final Long lineId) {
        this.lineId = lineId;
    }

    public Long getLineId() {
        return lineId;
    }
}
