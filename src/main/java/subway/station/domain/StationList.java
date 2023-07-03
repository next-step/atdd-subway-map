package subway.station.domain;

import java.util.List;
import subway.line.domain.Line;

public class StationList {

    private final List<Station> stations;

    public StationList(List<Station> stationList) {
        stations = stationList;
    }

    public void updateLine(Line line) {
        stations.forEach(station -> station.updateLine(line));
    }

    public List<Station> getStations() {
        return stations;
    }
}
