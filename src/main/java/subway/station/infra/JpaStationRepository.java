package subway.station.infra;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.station.domain.Station;
import subway.station.domain.StationCommandRepository;
import subway.station.domain.StationQueryRepository;

public interface JpaStationRepository
        extends StationQueryRepository, StationCommandRepository, JpaRepository<Station, Long> {
}
