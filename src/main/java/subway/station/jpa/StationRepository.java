package subway.station.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.line.jpa.Line;

import java.util.List;
import java.util.Optional;

public interface StationRepository extends JpaRepository<Station, Long> {

    Optional<Station> findByName(String name);

    List<Station> findByLine(Line line);
}