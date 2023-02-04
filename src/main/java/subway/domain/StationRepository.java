package subway.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.domain.Station;

public interface StationRepository extends JpaRepository<Station, Long> {
}
