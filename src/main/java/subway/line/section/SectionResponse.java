package subway.line.section;

import java.util.List;

import subway.station.dto.StationResponse;

public class SectionResponse {
    private Long id;
    private List<StationResponse> stations;

    public SectionResponse(Long id, List<StationResponse> stations) {
        this.id = id;
        this.stations = stations;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public void setStations(List<StationResponse> stations) {
        this.stations = stations;
    }
}
