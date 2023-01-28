package subway.station.infra;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.station.domain.Station;
import subway.station.domain.StationRepository;

public interface JpaStationRepository extends StationRepository, JpaRepository<Station, Long> {
}
