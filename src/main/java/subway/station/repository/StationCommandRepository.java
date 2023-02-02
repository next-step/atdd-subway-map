package subway.station.repository;

import subway.station.domain.Station;


public interface StationCommandRepository {

    Station save(Station station);

    void deleteById(Long id);

}