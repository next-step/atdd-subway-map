package subway.line;

import subway.station.StationResponse;

import java.util.List;

public class LineResponse {

    private final Long id;
    private final String name;
    private final List<StationResponse> stationResponseList;

    public LineResponse(Long id, String name, List<StationResponse> stationResponseList) {
        this.id = id;
        this.name = name;
        this.stationResponseList = stationResponseList;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<StationResponse> getStationResponseList() {
        return stationResponseList;
    }
}
