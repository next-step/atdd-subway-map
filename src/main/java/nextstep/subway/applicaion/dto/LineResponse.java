package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Station;

import java.util.List;

import static java.util.stream.Collectors.*;

public class LineResponse {

    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations;

    private LineResponse(Line line, List<Station> stations) {
        this.id = line.getId();
        this.name = line.getName();
        this.color = line.getColor();
        this.stations = stations.stream()
                .map(station -> StationResponse.from(station.getId(), station.getName()))
                .collect(toList());
    }

    public static LineResponse from(Line line, List<Station> stations) {
        return new LineResponse(line, stations);
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
