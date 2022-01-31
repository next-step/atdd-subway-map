package nextstep.subway.domain.section.dto;

import nextstep.subway.domain.section.Section;

public class SectionResponse {
    private final String downStationId;
    private final String upStationId;
    private final int distance;

    private SectionResponse(Long downStationId, Long upStationId, int distance) {
        this.downStationId = String.valueOf(downStationId);
        this.upStationId = String.valueOf(upStationId);
        this.distance = distance;
    }

    public static SectionResponse from(Section section) {
        return new SectionResponse(section.getDownStationId(), section.getUpStationId(), section.getDistance());
    }

    public String getDownStationId() {
        return downStationId;
    }

    public String getUpStationId() {
        return upStationId;
    }

    public int getDistance() {
        return distance;
    }
}
