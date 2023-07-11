package subway.line.dto;

import subway.line.domain.Section;

public class SectionRequest {

    private long upStationId;
    private long downStationId;
    private int distance;

    public long getUpStationId() {
        return upStationId;
    }

    public long getDownStationId() {
        return downStationId;
    }

    public int getDistance() {
        return distance;
    }
}
