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

    public LineResponse(Line subwayLine) {
        id = subwayLine.getId();
        name = subwayLine.getName();
        color = subwayLine.getColor();
        stations = subwayLine.getStations()
                .stream()
                .map(stationToSubwayLine -> new StationResponse(stationToSubwayLine.getStation()))
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
