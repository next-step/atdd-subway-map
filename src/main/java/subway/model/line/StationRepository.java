package subway.model.line;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.model.line.Station;

public interface StationRepository extends JpaRepository<Station, Long> {
}