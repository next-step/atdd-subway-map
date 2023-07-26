package subway.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.domain.Section;

public interface SectionRepository extends JpaRepository<Section, Long> {

    void deleteAllByStartStationId(Long startStationId);

}
