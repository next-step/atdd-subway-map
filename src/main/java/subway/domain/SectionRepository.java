package subway.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import subway.application.Section;

public interface SectionRepository extends JpaRepository<Section, Long> {
}
