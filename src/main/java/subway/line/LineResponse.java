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

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public static LineResponse fromLine(Line line, Station upStation, Station downStation){
        LineResponse res = new LineResponse();

        res.setId(line.getId());
        res.setName(line.getName());
        res.setColor(line.getColor());

        StationResponse upStationAttr = new StationResponse(upStation.getId(), upStation.getName());
        StationResponse downStationAttr = new StationResponse(downStation.getId(), downStation.getName());

        res.stations.add(upStationAttr);
        res.stations.add(downStationAttr);

        return res;
    }
}
