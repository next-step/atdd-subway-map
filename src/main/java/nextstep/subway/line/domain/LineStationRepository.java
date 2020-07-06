package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LineStationRepository extends CrudRepository<LineStation, Long> {
    Optional<LineStation> findByStation(Station station);
}
