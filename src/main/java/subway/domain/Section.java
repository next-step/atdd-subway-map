package subway.domain;

public class Section {

    private Long id;
    private Long downStationId;
    private Long upStationId;
    private Long distance;
    private Long lineId;

    public Section(Long id, Long downStationId, Long upStationId, Long distance, Long lineId) {
        this.id = id;
        this.downStationId = downStationId;
        this.upStationId = upStationId;
        this.distance = distance;
        this.lineId = lineId;
    }

    public static Section withNoId(Long downStationId, Long upStationId, Long distance, Long lineId) {
        return new Section(null, downStationId, upStationId, distance, lineId);
    }

    public Long getId() {
        return id;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDistance() {
        return distance;
    }

    public Long getLineId() {
        return lineId;
    }

}
