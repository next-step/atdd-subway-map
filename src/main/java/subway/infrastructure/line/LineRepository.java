package subway.infrastructure.line;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import subway.domain.line.Line;

import java.util.Optional;

public interface LineRepository extends JpaRepository<Line, Long> {

    @Query("SELECT l FROM Line l " +
            "LEFT JOIN FETCH l.sections.sections s " +
            "LEFT JOIN FETCH s.upStation " +
            "LEFT JOIN FETCH s.downStation " +
            "WHERE l.id = :lineId")
    Optional<Line> findByIdWithStations(Long lineId);
}
