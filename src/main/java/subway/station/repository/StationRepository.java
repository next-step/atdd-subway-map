package subway.station.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.station.domain.Station;

import java.util.Optional;

public interface StationRepository extends JpaRepository<Station, Long> {

    Optional<Station> findByName(String name);

}
