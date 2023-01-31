package subway.web.response;

import subway.domain.Line;
import subway.domain.Station;

import java.util.List;

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

    public static CreateLineResponse from(Line loadLine) {
        Station upStation = loadLine.getUpStation();
        Station downStation = loadLine.getDownStation();

        List<StationResponse> stations = List.of(new StationResponse(upStation.getId(), upStation.getName()), new StationResponse(downStation.getId(), downStation.getName()));
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
