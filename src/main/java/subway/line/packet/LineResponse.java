package subway.line.packet;

import subway.line.domain.Line;
import subway.station.domain.Station;
import subway.station.packet.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations;

    private LineResponse(Long id, String name, String color, List<Station> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations.stream().map(o -> new StationResponse(o.getId(), o.getName())).collect(Collectors.toList());
    }

    public static LineResponse fromEntity(Line line, Station upStation, Station downStation){
        return new LineResponse(line.getId(), line.getName(), line.getColor(), List.of(upStation, downStation));
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
