package nextstep.subway.applicaion.dto.section;

import nextstep.subway.domain.Section;

public class SectionResponse {

    private final Long upStationId;

    private final Long downStationId;

    private final Integer distance;

    public SectionResponse(Section section) {
        this.upStationId = section.getUpStation().getId();
        this.downStationId = section.getDownStation().getId();
        this.distance = section.getDistance();
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
