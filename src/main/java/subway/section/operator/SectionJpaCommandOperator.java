package subway.section.operator;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.section.domain.Section;
import subway.section.repository.SectionCommandRepository;

public interface SectionJpaCommandOperator
        extends SectionCommandRepository, JpaRepository<Section, Long> {
}