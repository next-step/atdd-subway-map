package subway.station.repository;

import subway.station.domain.Station;

import java.util.List;
import java.util.Optional;


public interface StationQueryRepository {

    Station save(Station station);

    List<Station> findAll();

    Optional<Station> findById(Long id);



}