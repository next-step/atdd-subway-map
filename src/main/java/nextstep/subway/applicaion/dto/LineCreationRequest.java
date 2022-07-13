package nextstep.subway.applicaion.dto;

import lombok.Getter;

@Getter
public class LineCreationRequest {
    private final String name;
    private final String color;
    private final Long upStationId;
    private final Long downStationId;
    private final Long distance;

    public LineCreationRequest(String name, String color, Long upStationId,
            Long downStationId, Long distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public SectionCreationRequest getSectionCreationRequest() {
        return new SectionCreationRequest(downStationId, upStationId, distance);
    }
}
