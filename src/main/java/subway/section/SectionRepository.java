package subway.section;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.station.Station;

public interface SectionRepository extends JpaRepository<Section, Long> {
}
