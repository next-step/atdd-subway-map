package subway.line.application;

import subway.line.presentation.AddSectionRequest;

public class SectionDto {
    private long downStationId;
    private long upStationId;
    private long distance;

    public SectionDto(long downStationId, long upStationId, long distance) {
        this.downStationId = downStationId;
        this.upStationId = upStationId;
        this.distance = distance;
    }

    public static SectionDto from(AddSectionRequest addSectionRequest) {
        return new SectionDto(
                addSectionRequest.getDownStationId(),
                addSectionRequest.getUpStationId(),
                addSectionRequest.getDistance()
        );
    }

    public long getDownStationId() {
        return downStationId;
    }

    public long getUpStationId() {
        return upStationId;
    }

    public long getDistance() {
        return distance;
    }
}
