package subway.section;

import java.util.HashSet;
import java.util.Set;
import subway.lines.Line;

public class SectionAddRequest {
    private Long upStationId;
    private Long downStationId;

    private Long distance;

    public SectionAddRequest(Long upStationId, Long downStationId, Long distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
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

    public Section getSection(Line line) {
       return new Section(line, upStationId, downStationId, distance);
    }
}
