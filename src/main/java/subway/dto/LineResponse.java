package subway.dto;

import java.util.List;
import subway.domain.Line;
import subway.domain.Station;

public class LineResponse {

    private Long id;

    private String name;

    private String color;

    private List<StationResponse> stations;

    public LineResponse() {

    }

    public LineResponse(Line line, Station upStation, Station downStation) {
        this.id = line.getId();
        this.name = line.getName();
        this.color = line.getColor();
        this.stations = List.of(
            StationResponse.from(upStation),
            StationResponse.from(downStation)
        );
    }

    public static LineResponse from(Line line, Station upStation, Station downStation) {
        return new LineResponse(line, upStation, downStation);
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
