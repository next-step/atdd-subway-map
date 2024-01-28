package subway.line.dto;

import subway.line.entity.Line;
import subway.station.entity.Station;
import subway.station.dto.StationResponse;

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

    public static LineResponse convertToDto(Line line) {
        List<StationResponse> stations = new ArrayList<>();

        Station upStation = line.getUpStation();
        if (upStation != null) {
            stations.add(new StationResponse(upStation.getId(), upStation.getName()));
        }

        Station downStation = line.getDownStation();
        if (downStation != null) {
            stations.add(new StationResponse(downStation.getId(), downStation.getName()));
        }

        return new LineResponse(line.getId(), line.getName(), line.getColor(), stations);
    }

}
