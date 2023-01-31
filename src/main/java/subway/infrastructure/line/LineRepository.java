package subway.infrastructure.line;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import subway.domain.line.Line;

import java.util.Optional;

public interface LineRepository extends JpaRepository<Line, Long> {

    @Query("select l from Line l left join fetch l.stations.stations where l.id = :lineId")
    Optional<Line> findByIdWithStations(Long lineId);
}
