package nextstep.subway.applicaion.dto;

public class SectionRequest {

    private Long id;
    private Long lineId;
    private Long upStationId;
    private Long downStationId;
    private int distance;

    public SectionRequest() {
    }

    private SectionRequest(Long id, Long lineId, Long upStationId, Long downStationId, int distance) {
        this.id = id;
        this.lineId = lineId;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public static SectionRequest of(Long lineId, Long upStationId, Long downStationId, int distance) {
        return new SectionRequest(null, lineId, upStationId, downStationId, distance);
    }

    public static SectionRequest valueOf(Long id, Long lineId) {
        return new SectionRequest(id, lineId, null, null, 0);

    }

    public Long getId() {
        return id;
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

    public int getDistance() {
        return distance;
    }
}
