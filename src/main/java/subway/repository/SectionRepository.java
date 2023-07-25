package subway.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.model.Section;

public interface SectionRepository extends JpaRepository<Section, Long> {
}
