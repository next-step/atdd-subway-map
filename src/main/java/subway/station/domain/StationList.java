package subway.station.domain;

import java.util.List;
import java.util.stream.Collectors;
import subway.line.domain.Line;
import subway.line.domain.LineStationConnection;

public class StationList {

    private final List<Station> stations;

    public StationList(List<Station> stationList) {
        stations = stationList;
    }

    public List<LineStationConnection> connectLineWithStation(Line line) {
        return stations.stream().map(station -> new LineStationConnection(line, station)).collect(Collectors.toList());
    }

    public List<Station> getStations() {
        return stations;
    }
}
