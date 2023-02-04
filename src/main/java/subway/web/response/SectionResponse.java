package subway.web.response;

import subway.domain.Section;

public class SectionResponse {

    private Long id;
    private Long downStationId;
    private Long upStationId;
    private Long distance;
    private Long lineId;

    public SectionResponse(Long id, Long downStationId, Long upStationId, Long distance, Long lineId) {
        this.id = id;
        this.downStationId = downStationId;
        this.upStationId = upStationId;
        this.distance = distance;
        this.lineId = lineId;
    }

    public static SectionResponse from(Section section) {
        return new SectionResponse(section.getId(), section.getDownStationId(), section.getUpStationId(), section.getDistance(), section.getLineId());
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
