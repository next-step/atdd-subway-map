package subway.section.repository;

import subway.section.domain.Section;

import java.util.Optional;

public interface SectionQueryRepository {

    Optional<Section> findById(Long id);

}