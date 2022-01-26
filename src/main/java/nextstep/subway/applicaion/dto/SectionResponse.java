package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Section;

public class SectionResponse {

    private Long id;
    private Long downStationId;
    private Long upStationId;
    private int distance;

    public static SectionResponse of(final Section section) {
        return new SectionResponse(section.getId(), section.getDownStation().getId(), section.getUpStation().getId(), section.getDistance());
    }

    private SectionResponse(final Long id, final Long downStationId, final Long upStationId, final int distance) {
        this.id = id;
        this.downStationId = downStationId;
        this.upStationId = upStationId;
        this.distance = distance;
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

    public int getDistance() {
        return distance;
    }
}
