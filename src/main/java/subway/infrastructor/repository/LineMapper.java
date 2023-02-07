package subway.infrastructor.repository;

import org.springframework.stereotype.Component;
import subway.domain.Line;

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
