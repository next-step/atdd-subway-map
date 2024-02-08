package subway.section;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.section.domain.Section;

public interface SectionRepository extends JpaRepository<Section, Long> {
}
