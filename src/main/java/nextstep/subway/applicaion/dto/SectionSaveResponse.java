package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Section;

public class SectionSaveResponse {

    private final Long downStationId;
    private final Long upStationId;
    private final int distance;

    public SectionSaveResponse(final Section section) {
        this(section.getDownStation().getId(), section.getUpStation().getId(), section.getDistance());
    }

    public SectionSaveResponse(final Long downStationId, final Long upStationId, final int distance) {
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

    public int getDistance() {
        return distance;
    }
}
