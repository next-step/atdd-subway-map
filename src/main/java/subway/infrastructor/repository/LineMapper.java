package subway.infrastructor.repository;

import org.springframework.stereotype.Component;
import subway.domain.LineCreateDto;
import subway.domain.Line;
import subway.domain.Station;

import java.util.List;

@Component
class LineMapper {

    LineJpaEntity domainToEntity(LineCreateDto lineCreate) {
        return new LineJpaEntity(lineCreate.getName(),
            lineCreate.getColor(),
            new StationPk(lineCreate.getUpStationId()),
            new StationPk(lineCreate.getDownStationId()),
            lineCreate.getDistance());
    }

    Line entityToDomain(LineJpaEntity lineJpaEntity, subway.infrastructor.repository.Station upStationJpaEntity, subway.infrastructor.repository.Station downStationJapEntity) {
        List<Station> stations =
            List.of(new Station(upStationJpaEntity.getId(), upStationJpaEntity.getName()), new Station(downStationJapEntity.getId(), downStationJapEntity.getName()));

        return new Line(lineJpaEntity.getId(),
            lineJpaEntity.getName(),
            lineJpaEntity.getColor(),
            stations);
    }

}
