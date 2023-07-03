package subway.dto;

import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;

public class SectionCreateRequest {

    private Long upStationId;
    private Long downStationId;
    private Integer distance;

    public SectionCreateRequest() {
    }

    public SectionCreateRequest(Long upStationId, Long downStationId, Integer distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public Section toSection(Line line, Station upStation, Station downStation) {
        return new Section(line, upStation, downStation, distance);
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
