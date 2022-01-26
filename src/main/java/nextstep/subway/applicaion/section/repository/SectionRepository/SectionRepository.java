package nextstep.subway.applicaion.section.repository.SectionRepository;

import nextstep.subway.applicaion.section.domain.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SectionRepository extends JpaRepository<Section, Long> {
}
