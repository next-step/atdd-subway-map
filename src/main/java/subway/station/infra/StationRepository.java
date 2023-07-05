package subway.station.infra;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.station.domain.Station;

public interface StationRepository extends JpaRepository<Station, Long> {
}