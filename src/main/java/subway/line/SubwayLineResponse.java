package subway.line;

import subway.station.Station;

import java.util.List;

public class SubwayLineResponse {

    private Long id;
    private String name;
    List<Station> stations;

    public SubwayLineResponse(Long id, String name, List<Station> stations) {
        this.id = id;
        this.name = name;
        this.stations = stations;
    }

    public Long getId() {
        return id;
    }
}