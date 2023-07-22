package subway.controller.dto;

import subway.domain.Line;

public class LineSectionCreateResponse {

    private Long upStationId;

    private Long downStationId;

    private Long distance;

    public LineSectionCreateResponse(Long upStationId, Long downStationId, Long distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public static LineSectionCreateResponse responseFrom(Line line) {
        return new LineSectionCreateResponse(
            line.getUpEndStation().getId(),
            line.getDownEndStation().getId(),
            line.getDistance()
        );
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDistance() {
        return distance;
    }
}
