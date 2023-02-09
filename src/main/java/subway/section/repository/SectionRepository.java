package subway.section.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.line.repository.Section;

public interface SectionRepository extends JpaRepository<Section, Long> {
}
