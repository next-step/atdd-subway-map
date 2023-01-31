package subway.infrastructor.repository;

import org.springframework.stereotype.Component;
import subway.domain.Line;
import subway.domain.Station;

@Component
class LineMapper {

    LineJpaEntity domainToEntity(Line line) {
        return new LineJpaEntity(
            line.getId(),
            line.getName(),
            line.getColor(),
            new StationPk(line.getUpStation().getId()),
            new StationPk(line.getDownStation().getId()),
            line.getDistance());
    }

    Line entityToDomain(LineJpaEntity lineJpaEntity, StationJpaEntity upStationJpaEntity, StationJpaEntity downStationJpaEntity) {
        return Line.withId(lineJpaEntity.getId(),
            lineJpaEntity.getName(),
            lineJpaEntity.getColor(),
            new Station(upStationJpaEntity.getId(), upStationJpaEntity.getName()),
            new Station(downStationJpaEntity.getId(), downStationJpaEntity.getName()),
            lineJpaEntity.getDistance());
    }

}
