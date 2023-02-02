package subway.line;

import subway.station.Station;
import subway.station.StationResponse;

import java.util.ArrayList;
import java.util.List;

public class LineResponse {

    private Long id;
    private String name;

    private String color;

    private List<StationResponse> stations = new ArrayList<>();


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

    public LineResponse(Long id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public static LineResponse fromLine(Line line, Station upStation, Station downStation) {
        LineResponse res = new LineResponse(line.getId(), line.getName(), line.getColor());

        StationResponse upStationAttr = new StationResponse(upStation.getId(), upStation.getName());
        StationResponse downStationAttr = new StationResponse(downStation.getId(), downStation.getName());

        List<StationResponse> lineStations = res.getStations();
        lineStations.add(upStationAttr);
        lineStations.add(downStationAttr);
        return res;
    }
}
