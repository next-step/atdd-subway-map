package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Section;

public class SectionRequest {
    private Long upStationId;
    private Long downStationId;
    private Integer distance;

    public SectionRequest(Long upStationId, Long downStationId, Integer distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public Section toEntity() {
        return new Section(this.upStationId, this.downStationId, this.distance);
    }
}
