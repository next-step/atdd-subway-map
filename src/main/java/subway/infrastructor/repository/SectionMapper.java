package subway.infrastructor.repository;

import org.springframework.stereotype.Component;
import subway.domain.Section;

@Component
public class SectionMapper {

    SectionJpaEntity domainToEntity(Section section) {
        return new SectionJpaEntity(
            new StationPk(section.getUpStationId()),
            new StationPk(section.getDownStationId()),
            section.getDistance(),
            new LinePk(section.getLineId()));
    }

    Section entityToDomain(SectionJpaEntity sectionJpaEntity) {
        return Section.withId(
            sectionJpaEntity.getId(), sectionJpaEntity.getDownStationId().getId(), sectionJpaEntity.getUpStationId().getId(), sectionJpaEntity.getDistance(), sectionJpaEntity.getLineId().getId()
        );
    }

}
