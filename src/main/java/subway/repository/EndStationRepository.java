package subway.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.domain.EndStation;

public interface EndStationRepository extends JpaRepository<EndStation, Long> {

    void deleteAllByStationId(Long stationId);

}
