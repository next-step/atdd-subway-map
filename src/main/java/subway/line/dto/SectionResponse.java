package subway.line.dto;

import subway.section.Section;

public class SectionResponse {

    private Long id;
    private Long upStationId;
    private Long downStationId;
    private Long distance;

    public SectionResponse(Long id, Long upStationId, Long downStationId, Long distance) {
        this.id = id;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public static SectionResponse from(Section section) {
        return new SectionResponse(section.getId(), section.getUpStation().getId(),
                section.getDownStation().getId(), section.getDistance());
    }

    public Long getId() {
        return id;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Long getDistance() {
        return distance;
    }
}
