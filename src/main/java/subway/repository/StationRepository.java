package subway.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.service.station.Station;

public interface StationRepository extends JpaRepository<Station, Long> {
}