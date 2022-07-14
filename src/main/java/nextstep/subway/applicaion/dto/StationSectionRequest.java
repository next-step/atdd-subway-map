package nextstep.subway.applicaion.dto;

public class StationSectionRequest {
    private final Long upStationId;
    private final Long downStationId;
    private final Integer distance;

    public StationSectionRequest(StationLineRequest request) {
        this(request.getUpStationId(), request.getDownStationId(), request.getDistance());
    }

    public StationSectionRequest(Long upStationId, Long downStationId, Integer distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Integer getDistance() {
        return distance;
    }
}
