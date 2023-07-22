package subway.controller.dto;

import subway.domain.LineSection;

public class LineSectionCreateResponse {
    private Long downStationId;

    private Long upStationId;

    private Long distance;

    public LineSectionCreateResponse() {}

    public LineSectionCreateResponse(Long downStationId, Long upStationId, Long distance) {
        this.downStationId = downStationId;
        this.upStationId = upStationId;
        this.distance = distance;
    }

    public static LineSectionCreateResponse responseFrom(LineSection lineSection) {
        return new LineSectionCreateResponse(lineSection.getSection().getDownStation().getId(),
            lineSection.getSection().getUpStation().getId(),
            lineSection.getSection().getDistance());
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
