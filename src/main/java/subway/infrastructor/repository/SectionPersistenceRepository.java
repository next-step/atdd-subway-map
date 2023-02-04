package subway.infrastructor.repository;

import org.springframework.stereotype.Component;
import subway.application.service.output.SectionCommandRepository;
import subway.domain.Section;

@Component
class SectionPersistenceRepository implements SectionCommandRepository {

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

}
