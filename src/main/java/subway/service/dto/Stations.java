package subway.service.dto;

import subway.exception.SubwayRuntimeException;
import subway.exception.message.SubwayErrorCode;
import subway.repository.entity.Station;

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
        throw new SubwayRuntimeException(SubwayErrorCode.NOT_FOUND_STATION);
    }
}