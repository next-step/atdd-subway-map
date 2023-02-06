package subway.line.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.line.entity.Station;

public interface StationRepository extends JpaRepository<Station, Long> {
}