package nextstep.subway.applicaion.dto.line;

import nextstep.subway.applicaion.dto.station.StationResponse;
import nextstep.subway.domain.Line;

import java.util.List;
import java.util.stream.Collectors;

public class LineResponse {

    private final Long id;

    private final String name;

    private final String color;

    private final List<StationResponse> stations;

    public LineResponse(Line line) {
        id = line.getId();
        name = line.getName();
        color = line.getColor();
        stations = line.getAllStations().stream()
                .map(StationResponse::new)
                .collect(Collectors.toList());
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
