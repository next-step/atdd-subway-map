package nextstep.subway.section.application.dto;

import nextstep.subway.section.domain.Section;

import javax.validation.constraints.Positive;

public class SectionRequest {
    @Positive
    private Long upStationId;
    @Positive
    private Long downStationId;
    @Positive
    private Integer distance;

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Integer getDistance() {
        return distance;
    }

    public Section toSection(Long lineId) {
        return new Section(lineId, upStationId, downStationId, distance);
    }
}
