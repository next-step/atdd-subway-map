package subway.line.response;

import lombok.Builder;
import lombok.Getter;
import subway.station.entity.Station;
import subway.station.response.StationResponse;

import java.util.List;

@Getter
public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations;

    @Builder
    public LineResponse(Long id, String name, String color, Station upStation, Station downStation) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = List.of(new StationResponse(upStation.getId(), upStation.getName()),
                new StationResponse(downStation.getId(), downStation.getName()));
    }
}
