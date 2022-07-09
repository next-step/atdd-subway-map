package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Station;

import java.util.ArrayList;
import java.util.List;

public class LineResponse {

    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations;

    public LineResponse(Long id, String name, String color, List<StationResponse> stations) {
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

    public List<StationResponse> getStations() {
        return stations;
    }

    public static LineResponse from(Line line) {
        List<StationResponse> stationResponses = new ArrayList<>();
        for (Station station : line.stations()) {
            stationResponses.add(StationResponse.from(station));
        }
        return new LineResponse(line.id(), line.name(), line.color(), stationResponses);
    }
}
