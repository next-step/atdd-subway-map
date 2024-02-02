package subway.station.service;

import subway.station.repository.domain.Station;

public interface StationProvider {
    Station findById(Long id);
}
