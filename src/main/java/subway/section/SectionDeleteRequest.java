package subway.section;

import java.util.List;
import java.util.Objects;
import subway.lines.Line;

public class SectionDeleteRequest {
    private final Long stationId;

    public SectionDeleteRequest(Long stationId) {
        this.stationId = stationId;
    }

    public Long getStationId() {
        return stationId;
    }

}
