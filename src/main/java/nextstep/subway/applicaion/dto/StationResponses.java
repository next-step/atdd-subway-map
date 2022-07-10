package nextstep.subway.applicaion.dto;

import java.util.Arrays;
import java.util.List;

public class StationResponses {
    private final List<StationResponse> stations;

    public StationResponses(List<StationResponse> stations) {
        this.stations = stations;
    }

    public List<StationResponse> getStations() {
        return List.copyOf(stations);
    }
}
