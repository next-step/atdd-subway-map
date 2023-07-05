package subway.section.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import subway.section.domain.Section;

@Repository
public interface SectionRepository extends JpaRepository<Section, Long> {
}
