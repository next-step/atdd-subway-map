package subway.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.models.Station;

public interface StationRepository extends JpaRepository<Station, Long> {
}