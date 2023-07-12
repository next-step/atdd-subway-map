package subway.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import subway.entity.Section;

public interface SectionRepository extends JpaRepository<Section, Long> {

    Optional<List<Section>> findAllByStationLineId(long stationLineId);

}
