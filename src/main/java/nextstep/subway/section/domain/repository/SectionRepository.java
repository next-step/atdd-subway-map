package nextstep.subway.section.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import nextstep.subway.section.domain.model.Section;

@Repository
public interface SectionRepository extends JpaRepository<Section, Long> {
}
