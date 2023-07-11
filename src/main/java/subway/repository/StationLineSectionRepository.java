package subway.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import subway.entity.StationLineSection;

public interface StationLineSectionRepository extends JpaRepository<StationLineSection, Long> {

    List<StationLineSection> findAllByStationLineId(long stationLineId);

}
