package subway.line.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import subway.line.domain.Section;

@Repository
public interface SectionRepository extends JpaRepository<Section, Long> {



}
