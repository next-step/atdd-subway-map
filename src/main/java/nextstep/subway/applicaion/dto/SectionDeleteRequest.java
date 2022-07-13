package nextstep.subway.applicaion.dto;

public class SectionDeleteRequest {

    private Long lineId;
    private Long downStationId;

    public SectionDeleteRequest(Long lineId, Long downStationId) {
        this.lineId = lineId;
        this.downStationId = downStationId;
    }

    public static SectionDeleteRequest of(Long lineId, Long stationId) {
        return new SectionDeleteRequest(lineId, stationId);
    }

    public Long getLineId() {
        return lineId;
    }

    public Long getDownStationId() {
        return downStationId;
    }
}
