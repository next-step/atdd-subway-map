package subway.station;

import subway.exception.SubwayNotFoundException;

import java.util.Map;

public class Stations {
    private Map<Long, Station> stations;

    public Stations(Map<Long, Station> stations) {
        this.stations = stations;
    }

    public Map<Long, Station> getStations() {
        return stations;
    }

    public Station findById(Long id) {
        if (stations.containsKey(id)) {
            return stations.get(id);
        }
        throw new SubwayNotFoundException("Station not found with id, " + id);
    }
}
