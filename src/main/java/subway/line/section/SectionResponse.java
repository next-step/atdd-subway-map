package subway.line.section;

import subway.station.StationResponse;

import java.util.List;

public class SectionResponse {
    private Long stationId;
    private String name;
    private Long distanceFromPrev;

    public SectionResponse(Long stationId, String name, Long distanceFromPrev) {
        this.stationId = stationId;
        this.name = name;
        this.distanceFromPrev = distanceFromPrev;
    }

    public Long getStationId() {
        return stationId;
    }

    public String getName() {
        return name;
    }

    public Long getDistanceFromPrev() {
        return distanceFromPrev;
    }
}
