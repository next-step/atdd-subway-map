package subway.web.request;

import subway.domain.SectionCreateDto;

public class SectionCreateRequest {

    private final Long downStationId;
    private final Long upStationId;
    private final Long distance;

    public SectionCreateRequest(Long downStationId, Long upStationId, Long distance) {
        this.downStationId = downStationId;
        this.upStationId = upStationId;
        this.distance = distance;
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

    public SectionCreateDto toDto(Long lineId) {
        return new SectionCreateDto(lineId, this.getUpStationId(), this.getDownStationId(), getDistance());
    }

}
