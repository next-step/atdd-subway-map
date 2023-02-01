package subway.station.web.dto;

import subway.station.domain.station.Station;

import java.util.List;

public class FindLineResponse {

    private Long id;
    private String name;
    private String color;
    private List<Station> stations;

    public FindLineResponse(Long id, String name, String color, List<Station> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
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

    public List<Station> getStations() {
        return stations;
    }
}
