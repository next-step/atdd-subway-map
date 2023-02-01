package subway.station.operator;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.station.domain.Station;
import subway.station.repository.StationQueryRepository;


public interface StationJpaQueryOperator
        extends StationQueryRepository, JpaRepository<Station, Long> {
}