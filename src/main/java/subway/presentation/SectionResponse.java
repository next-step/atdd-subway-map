package subway.presentation;

import subway.domain.Section;

public class SectionResponse {
    private final Long lineId;
    private final Long sectionId;
    private final Long upStationId;
    private final Long downStationId;
    private final Integer distance;

    private SectionResponse(Long lineId, Long sectionId, Long upStationId, Long downStationId, Integer distance) {
        this.lineId = lineId;
        this.sectionId = sectionId;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public Long getSectionId() {
        return sectionId;
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

    public Integer getDistance() {
        return distance;
    }

    public static SectionResponse of(Section createdSection) {
        return new SectionResponse(
                createdSection.getLine().getId(),
                createdSection.getId(),
                createdSection.getUpStation().getId(),
                createdSection.getDownStation().getId(),
                createdSection.getDistance()
        );
    }

}
