package subway.infrastructor.repository;

import org.springframework.stereotype.Component;
import subway.domain.LineCreateDomain;
import subway.domain.LineDomain;
import subway.domain.StationDomain;

import java.util.List;

@Component
class LineMapper {

    LineJpaEntity domainToEntity(LineCreateDomain lineCreateDomain) {
        return new LineJpaEntity(lineCreateDomain.getName(),
            lineCreateDomain.getColor(),
            new StationPk(lineCreateDomain.getUpStationId()),
            new StationPk(lineCreateDomain.getDownStationId()),
            lineCreateDomain.getDistance());
    }

    LineDomain entityToDomain(LineJpaEntity lineJpaEntity, Station upStationJpaEntity, Station downStationJapEntity) {
        List<StationDomain> stationDomains =
            List.of(new StationDomain(upStationJpaEntity.getId(), upStationJpaEntity.getName()), new StationDomain(downStationJapEntity.getId(), downStationJapEntity.getName()));

        return new LineDomain(lineJpaEntity.getId(),
            lineJpaEntity.getName(),
            lineJpaEntity.getColor(),
            stationDomains);
    }

}
