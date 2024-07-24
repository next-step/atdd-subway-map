package subway.section.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.section.entity.Section;

import java.util.Optional;

public interface SectionRepository extends JpaRepository<Section, Long> {

    Optional<Section> findByDownStationId(Long downStationId);
}
