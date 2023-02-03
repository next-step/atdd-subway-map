package subway.domain;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SectionRepository extends JpaRepository<Section, Long> {

	Optional<Section> findSectionByDownStationId(Long downStationId);
}
