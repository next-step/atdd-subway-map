package subway.station.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.line.entity.Line;

import java.util.List;
import java.util.Optional;

public interface StationRepository extends JpaRepository<Station, Long> {

    Optional<Station> findByName(String name);
}