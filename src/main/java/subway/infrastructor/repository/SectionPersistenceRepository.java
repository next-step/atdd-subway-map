package subway.infrastructor.repository;

import org.springframework.stereotype.Component;
import subway.application.service.output.SectionCommandRepository;
import subway.application.service.output.SectionLoadRepository;
import subway.domain.NotFoundSectionException;
import subway.domain.Section;

import java.util.Optional;

@Component
class SectionPersistenceRepository implements SectionCommandRepository, SectionLoadRepository {

    private final SectionMapper mapper;
    private final SectionRepository sectionRepository;

    public SectionPersistenceRepository(SectionMapper mapper, SectionRepository sectionRepository) {
        this.mapper = mapper;
        this.sectionRepository = sectionRepository;
    }

    @Override
    public Long createSection(Section section) {
        SectionJpaEntity save = sectionRepository.save(mapper.domainToEntity(section));
        return save.getId();
    }

    @Override
    public Section loadSection(Long sectionId) {
        SectionJpaEntity sectionJpaEntity = sectionRepository.findById(sectionId).orElseThrow(NotFoundSectionException::new);
        return mapper.entityToDomain(sectionJpaEntity);
    }

}
