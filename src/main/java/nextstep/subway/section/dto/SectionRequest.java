package nextstep.subway.section.dto;

import nextstep.subway.line.domain.Line;
import nextstep.subway.section.domain.Section;

public class SectionRequest {

    Long downStationId;

    Long upStationId;

    int distance;

    public SectionRequest() {
    }

    public SectionRequest(Long downStationId, Long upStationId, int distance) {
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
