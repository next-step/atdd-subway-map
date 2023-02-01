package subway.section;

import subway.line.Line;
import subway.line.LineRequest;

public class SectionRequest {
    private Long upStationId;
    private Long downStationId;
    private int distance;

    private SectionRequest() {
    }

    public SectionRequest(Long upStationId, Long downStationId, int distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public Section toEntity(Line line) {
        return new Section(line, upStationId, downStationId, distance);
    }

    public static SectionRequest toRequest(Line line) {
        return new SectionRequest(line.getUpStationId(), line.getDownStationId(), line.getDistance());
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public int getDistance() {
        return distance;
    }
}
