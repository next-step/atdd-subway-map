package subway.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import subway.entity.StationLineSection;

public interface StationLineSectionRepository extends JpaRepository<StationLineSection, Long> {

    Optional<List<StationLineSection>> findAllByStationLineId(long stationLineId);

}
