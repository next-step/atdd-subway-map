package subway.web.response;

import subway.domain.LineDomain;

import java.util.List;
import java.util.stream.Collectors;

public class CreateLineResponse {

    private final Long id;
    private final String name;
    private final String color;
    private final List<StationResponse> stations;

    public CreateLineResponse(Long id, String name, String color, List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public static CreateLineResponse from(LineDomain loadLine) {
        List<StationResponse> stations = loadLine.getStations().stream().map(stationDomain -> new StationResponse(stationDomain.getId(), stationDomain.getName())).collect(Collectors.toList());
        return new CreateLineResponse(loadLine.getId(), loadLine.getName(), loadLine.getColor(), stations);
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
