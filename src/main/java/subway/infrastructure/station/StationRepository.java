package subway.infrastructure.station;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.domain.station.entity.Station;

public interface StationRepository extends JpaRepository<Station, Long> {
}