package subway.station.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.station.repository.domain.Station;

public interface StationRepository extends JpaRepository<Station, Long> {
}
