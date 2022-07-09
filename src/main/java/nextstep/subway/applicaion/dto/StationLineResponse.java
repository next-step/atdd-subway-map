package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.StationLine;

import java.util.List;

public class StationLineResponse {
    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations;

    public StationLineResponse(Long id, String name, String color, List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public StationLineResponse(StationLine stationLine, StationResponse upStation, StationResponse downStation) {
        this(stationLine.getId(), stationLine.getName(), stationLine.getColor(), List.of(upStation, downStation));
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
