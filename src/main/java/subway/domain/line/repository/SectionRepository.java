package subway.domain.line.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.domain.line.domain.Section;

public interface SectionRepository extends JpaRepository<Section, Long> {
}
