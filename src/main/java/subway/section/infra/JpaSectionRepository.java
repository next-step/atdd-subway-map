package subway.section.infra;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.section.domain.Section;
import subway.section.domain.SectionCommandRepository;

public interface JpaSectionRepository
        extends SectionCommandRepository, JpaRepository<Section, Long> {
}
