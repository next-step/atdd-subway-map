package subway.station.operator;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.station.domain.Station;
import subway.station.repository.StationCommandRepository;

public interface StationJpaCommandOperator
        extends StationCommandRepository, JpaRepository<Station, Long> {
}