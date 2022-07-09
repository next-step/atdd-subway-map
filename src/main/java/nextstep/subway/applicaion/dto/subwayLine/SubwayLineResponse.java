package nextstep.subway.applicaion.dto.subwayLine;

import nextstep.subway.applicaion.dto.station.StationResponse;
import nextstep.subway.domain.subwayLine.SubwayLine;

import java.util.List;
import java.util.stream.Collectors;

public class SubwayLineResponse {

    private final Long id;

    private final String name;

    private final String color;

    private final List<StationResponse> stations;

    public SubwayLineResponse(SubwayLine subwayLine) {
        id = subwayLine.getId();
        name = subwayLine.getName();
        color = subwayLine.getColor().getCode();
        stations = subwayLine.getStations()
                .stream()
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
