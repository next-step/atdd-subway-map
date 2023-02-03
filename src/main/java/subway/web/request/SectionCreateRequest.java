package subway.web.request;

import subway.domain.SectionCreateDto;

public class SectionCreateRequest {

    private final String downStationId;
    private final String upStationId;
    private final Long distance;

    public SectionCreateRequest(String downStationId, String upStationId, Long distance) {
        this.downStationId = downStationId;
        this.upStationId = upStationId;
        this.distance = distance;
    }

    public String getDownStationId() {
        return downStationId;
    }

    public String getUpStationId() {
        return upStationId;
    }

    public Long getDistance() {
        return distance;
    }

    public SectionCreateDto toDto(Long lineId) {
        return new SectionCreateDto(lineId, Long.parseLong(this.getUpStationId()), Long.parseLong(this.getDownStationId()), getDistance());
    }

}
