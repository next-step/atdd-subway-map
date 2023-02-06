package subway.station.application.dto;

import subway.exception.StationNotFoundException;
import subway.station.domain.Station;

import java.util.Map;

public class Stations {
    private Map<Long, Station> stations;

    public Stations(Map<Long, Station> stations) {
        this.stations = stations;
    }

    public Station getById(Long id) {
        if (stations.containsKey(id)) {
            return stations.get(id);
        }
        throw new StationNotFoundException(id);
    }
}
