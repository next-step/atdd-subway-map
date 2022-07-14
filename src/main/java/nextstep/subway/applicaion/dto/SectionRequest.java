package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Section;
import nextstep.subway.domain.StationLine;

public class SectionRequest {
    private Long downStationId;
    private Long upStationId;
    private Long distance;

    public Long getDownStationId() {
        return downStationId;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDistance() {
        return distance;
    }

    public Section toEntity(StationLine stationLine){
        return new Section(this.distance, this.upStationId, this.downStationId, stationLine);
    }
}
