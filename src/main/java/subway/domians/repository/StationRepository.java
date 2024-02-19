package subway.domians.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.domians.domain.Station;

public interface StationRepository extends JpaRepository<Station, Long> {

}
