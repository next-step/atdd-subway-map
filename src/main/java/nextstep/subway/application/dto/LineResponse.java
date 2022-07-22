package nextstep.subway.application.dto;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Station;

import java.util.ArrayList;
import java.util.List;

public class LineResponse {
    private final Long id;
    private final String name;
    private final String color;
    private final List<StationResponse> stations = new ArrayList<>();

    public LineResponse(Line line) {
        this.id = line.getId();
        this.name = line.getName();
        this.color = line.getColor();
        List<Station> stations = line.getStations();
        for (Station station : stations) {
            this.stations.add(new StationResponse(station.getId(), station.getName()));
        }
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

    public List<StationResponse> getStations() {
        return stations;
    }

}
