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
            line.getColor());
    }

    Line entityToDomain(LineJpaEntity lineJpaEntity) {
        return Line.withId(lineJpaEntity.getId(),
            lineJpaEntity.getName(),
            lineJpaEntity.getColor());
    }

}
