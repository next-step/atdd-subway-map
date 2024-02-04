package subway.infrastructure.line;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.domain.line.entity.Section;

public interface SectionRepository extends JpaRepository<Section, Long> {
}
