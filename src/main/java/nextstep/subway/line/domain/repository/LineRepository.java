package nextstep.subway.line.domain.repository;

import java.util.Optional;
import nextstep.subway.line.domain.model.Line;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LineRepository extends JpaRepository<Line, Long> {
    boolean existsByName(String name);

    @Query("SELECT l FROM Line l"
        + " JOIN FETCH l.sections.values s"
        + " JOIN FETCH s.upStation"
        + " JOIN FETCH s.downStation"
        + " WHERE l.id = :id")
    Optional<Line> findByIdWithStations(long id);
}
