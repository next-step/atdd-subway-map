package subway.dto;

import java.util.List;

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

    public List<StationResponse> getStations() {
        return stations;
    }
}
