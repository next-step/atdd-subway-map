package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Section;

public class SectionResponse {
    private String downStationId;
    private String upStationId;
    private int distance;

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
