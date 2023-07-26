package subway.controller.dto;

import subway.domain.Line;

public class LineSectionDeleteResponse {

    private Long upStationId;

    private Long downStationId;

    private Long distance;

    public LineSectionDeleteResponse(Long upStationId, Long downStationId, Long distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public static LineSectionDeleteResponse responseFrom(Line line) {
        return new LineSectionDeleteResponse(
            line.getUpEndStation().getId(),
            line.getDownEndStation().getId(),
            line.getDistance()
        );
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Long getDistance() {
        return distance;
    }
}
