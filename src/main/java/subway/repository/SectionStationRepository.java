package subway.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.domain.sectionstation.Direction;
import subway.domain.sectionstation.SectionStation;
import subway.domain.station.Station;

import java.util.Optional;

public interface SectionStationRepository extends JpaRepository<SectionStation, Long> {
    Optional<SectionStation> findByStationAndDirection(Station station, Direction direction);
}
