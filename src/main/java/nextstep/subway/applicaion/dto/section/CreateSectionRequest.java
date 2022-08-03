package nextstep.subway.applicaion.dto.section;

public class CreateSectionRequest {

    private final Long upStationId;

    private final Long downStationId;

    private final Integer distance;

    private CreateSectionRequest() {
        this.upStationId = null;
        this.downStationId = null;
        this.distance = null;
    }

    public CreateSectionRequest(Long upStationId, Long downStationId, Integer distance) {
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
