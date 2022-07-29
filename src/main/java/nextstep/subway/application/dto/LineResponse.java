package nextstep.subway.application.dto;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Station;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LineResponse {
    private final Long id;
    private final String name;
    private final String color;
    private final List<StationResponse> stations = new ArrayList<>();

    public LineResponse(Line line, List<Station> stationList) {
        this.id = line.getId();
        this.name = line.getName();
        this.color = line.getColor();
        this.stations.addAll(
                stationList.stream()
                        .map(station -> new StationResponse(station.getId(), station.getName()))
                        .collect(Collectors.toList())
        );

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
