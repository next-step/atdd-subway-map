package subway.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import subway.domain.Section;

@Repository
public interface SectionRepository extends JpaRepository<Section,Long> {
}
