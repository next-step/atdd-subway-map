package subway.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.entity.Section;

public interface SectionRepository extends JpaRepository<Section, Long> {
}
