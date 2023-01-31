package subway.web.response;

import subway.domain.Line;

import java.util.List;
import java.util.stream.Collectors;

public class LineResponse {

    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations;

    public LineResponse() {
    }

    public LineResponse(Long id, String name, String color, List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public static LineResponse from(Line loadLine) {
        List<StationResponse> stations = loadLine.getStations().stream().map(stationDomain -> new StationResponse(stationDomain.getId(), stationDomain.getName())).collect(Collectors.toList());
        return new LineResponse(loadLine.getId(), loadLine.getName(), loadLine.getColor(), stations);
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
