package subway.station;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface StationRepository extends JpaRepository<Station, Long> {
    List<Station> findByIdIn(List<Long> ids);
}
