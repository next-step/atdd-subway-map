package subway.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.models.Section;

public interface SectionRepository extends JpaRepository<Section, Long> {
}