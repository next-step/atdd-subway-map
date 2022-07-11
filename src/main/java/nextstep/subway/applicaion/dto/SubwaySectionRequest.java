package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Section;

public class SubwaySectionRequest {
    private Long upStationId;
    private Long downStationId;
    private Integer distance;

    public SubwaySectionRequest(Long upStationId, Long downStationId, Integer distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public Section toEntity() {
        return new Section(this.upStationId, this.downStationId, this.distance);
    }
}
