package subway.station;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface StationRepository extends JpaRepository<Station, Long> {
}