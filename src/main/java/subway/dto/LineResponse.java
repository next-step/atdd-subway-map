package subway.dto;

import subway.domain.Station;

import java.util.ArrayList;
import java.util.List;

public class LineResponse {

    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stationResponses = new ArrayList<>();

    public LineResponse(Long id, String name, String color, List<Station> endToEndStations) {
        this.id = id;
        this.name = name;
        this.color = color;
        createStationResponse(endToEndStations);
    }

    private void createStationResponse(List<Station> stations) {
        stations.stream()
                .map(StationResponse::from)
                .forEachOrdered(stationResponses::add);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<StationResponse> getStationResponses() {
        return stationResponses;
    }
}
