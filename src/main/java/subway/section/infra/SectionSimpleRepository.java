package subway.section.infra;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.section.domain.Section;

public interface SectionSimpleRepository extends SectionRepository, JpaRepository<Section, Long> {

}
