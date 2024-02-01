package subway;

import org.springframework.data.jpa.repository.JpaRepository;

import subway.domain.Station;
import subway.domain.Stations;

public interface StationRepository extends JpaRepository<Station, Long> {
}