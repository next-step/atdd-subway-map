package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.SubwayLine;

import java.util.List;

public class SubwayLineResponse {
    private final Long id;
    private final String name;
    private final String color;
    private final List<StationResponse> stations;

    public SubwayLineResponse(Long id, String name, String color, List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public SubwayLineResponse(SubwayLine subwayLine, StationResponse upStation, StationResponse downStation) {
        this(subwayLine.getId(), subwayLine.getName(), subwayLine.getColor(), List.of(upStation, downStation));
    }

    public long getId() {
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
