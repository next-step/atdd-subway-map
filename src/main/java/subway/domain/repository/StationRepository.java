package subway.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.domain.entity.Station;

public interface StationRepository extends JpaRepository<Station, Long> {
}