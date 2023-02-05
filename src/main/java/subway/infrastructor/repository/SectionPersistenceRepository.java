package subway.infrastructor.repository;

import org.springframework.stereotype.Component;
import subway.application.service.output.LineLoadRepository;
import subway.application.service.output.SectionCommandRepository;
import subway.application.service.output.SectionLoadRepository;
import subway.domain.Line;
import subway.domain.NotFoundSectionException;
import subway.domain.NotFoundStationException;
import subway.domain.Section;

import java.util.ArrayList;
import java.util.List;

@Component
class SectionPersistenceRepository implements SectionCommandRepository, SectionLoadRepository {

    private final SectionMapper mapper;
    private final SectionRepository sectionRepository;
    private final LineLoadRepository lineLoadRepository;
    private final StationRepository stationRepository;

    public SectionPersistenceRepository(SectionMapper mapper, SectionRepository sectionRepository, LineLoadRepository lineLoadRepository, StationRepository stationRepository) {
        this.mapper = mapper;
        this.sectionRepository = sectionRepository;
        this.lineLoadRepository = lineLoadRepository;
        this.stationRepository = stationRepository;
    }

    @Override
    public Long createSection(Section section) {
        SectionJpaEntity save = sectionRepository.save(mapper.domainToEntity(section));
        return save.getId();
    }

    @Override
    public void deleteSection(Long id) {
        sectionRepository.deleteById(id);
    }

    @Override
    public Section loadSection(Long sectionId) {
        SectionJpaEntity sectionJpaEntity = sectionRepository.findById(sectionId).orElseThrow(NotFoundSectionException::new);
        return buildSection(sectionJpaEntity);
    }

    @Override
    public List<Section> loadLineSection(Long loadLineId) {
        List<Section> sections = new ArrayList<>();

        sectionRepository.findByLineId(new LinePk(loadLineId)).forEach(sectionJpaEntity -> {
            Section section = buildSection(sectionJpaEntity);
            sections.add(section);
        });

        return sections;
    }

    @Override
    public Section loadLineSectionWithLineId(Long lineId, Long sectionId) {
        SectionJpaEntity sectionJpaEntity = sectionRepository.findByIdAndLineId(sectionId, new LinePk(lineId)).orElseThrow(NotFoundSectionException::new);
        return buildSection(sectionJpaEntity);
    }

    private Section buildSection(SectionJpaEntity sectionJpaEntity) {
        Line line = lineLoadRepository.loadLine(sectionJpaEntity.getLineId().getId());
        StationJpaEntity upStation = stationRepository.findById(sectionJpaEntity.getUpStationId().getId()).orElseThrow(NotFoundStationException::new);
        StationJpaEntity downStation = stationRepository.findById(sectionJpaEntity.getDownStationId().getId()).orElseThrow(NotFoundStationException::new);

        return mapper.entityToDomain(sectionJpaEntity, upStation, downStation, line);
    }

}
