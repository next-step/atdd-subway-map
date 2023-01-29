package subway.infrastructor.repository;

import org.springframework.stereotype.Component;
import subway.domain.LineCreateDomain;

@Component
class LineMapper {

    LineJpaEntity domainToEntity(LineCreateDomain lineCreateDomain) {
        return new LineJpaEntity(lineCreateDomain.getName(),
            lineCreateDomain.getColor(),
            new StationPk(lineCreateDomain.getUpStationId()),
            new StationPk(lineCreateDomain.getDownStationId()),
            lineCreateDomain.getDistance());
    }

}
