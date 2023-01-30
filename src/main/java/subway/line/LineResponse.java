package subway.line;

import subway.station.Station;
import subway.station.StationResponse;

import java.util.List;

public class LineResponse {

    private Long id;
    private String name;

    private String color;

    private List<StationAttr> stations;


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<StationAttr> getStations() {
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

    public static LineResponse toLineResponse(Line line, Station upStation, Station downStation){
        LineResponse res = new LineResponse();

        res.setId(line.getId());
        res.setName(line.getName());
        res.setColor(line.getColor());

        StationAttr upStationAttr = StationAttr.toStationAttr(upStation);
        StationAttr downStationAttr = StationAttr.toStationAttr(downStation);

        res.stations.add(upStationAttr);
        res.stations.add(downStationAttr);

        return res;
    }


    static class StationAttr{
        private Long id;
        private String name;

        public StationAttr(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public static StationAttr toStationAttr(Station station){
            return new StationAttr(station.getId(), station.getName());
        }
    }
}
