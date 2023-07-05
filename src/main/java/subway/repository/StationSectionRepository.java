package subway.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.domain.Section;

public interface StationSectionRepository extends JpaRepository<Section, Long> {
}
