package subway.ui.dto;

import subway.domain.Section;

public class SectionResponse {

    private Long upStationId;
    private Long downStationId;
    private Integer distance;

    public SectionResponse(final Long upStationId, final Long downStationId, final Integer distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public static SectionResponse from(final Section section) {
        return new SectionResponse(section.getUpStation().getId(), section.getDownStation().getId(), section.getDistance());
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
