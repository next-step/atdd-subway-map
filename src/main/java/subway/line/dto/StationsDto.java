package subway.line.dto;

import java.util.List;

import subway.station.dto.StationResponse;

public class StationsDto {
    private List<StationResponse> stations;

    public StationsDto(List<StationResponse> stations) {
        this.stations = stations;
    }

    public List<StationResponse> getStations() {
        return stations;
    }
}
