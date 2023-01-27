package subway.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.model.Station;

public interface StationRepository extends JpaRepository<Station, Long> {
}
