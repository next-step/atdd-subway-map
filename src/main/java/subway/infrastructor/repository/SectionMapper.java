package subway.infrastructor.repository;

import org.springframework.stereotype.Component;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;

@Component
public class SectionMapper {

    SectionJpaEntity domainToEntityWithId(Section section) {
        return new SectionJpaEntity(
            section.getId(),
            new StationPk(section.getUpStation().getId()),
            new StationPk(section.getDownStation().getId()),
            section.getDistance(),
            new LinePk(section.getLine().getId()));
    }

    SectionJpaEntity domainToEntity(Section section) {
        return new SectionJpaEntity(
            new StationPk(section.getUpStation().getId()),
            new StationPk(section.getDownStation().getId()),
            section.getDistance(),
            new LinePk(section.getLine().getId()));
    }

    Section entityToDomain(SectionJpaEntity sectionJpaEntity, StationJpaEntity upStation, StationJpaEntity downStation, Line line) {
        return Section.withId(
            sectionJpaEntity.getId(),
            new Station(downStation.getId(), downStation.getName()),
            new Station(upStation.getId(), upStation.getName()),
            sectionJpaEntity.getDistance(),
            line);
    }

}
