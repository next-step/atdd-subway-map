package subway.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import subway.domain.Station;

public interface StationRepository extends JpaRepository<Station, Long> {

}
