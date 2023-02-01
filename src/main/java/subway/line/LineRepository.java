package subway.line;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LineRepository extends JpaRepository<Line, Long> {
    @Query(value = "select distinct l from Line l left join fetch l.stations")
    List<Line> findAllWithStation();

    @Query(value = "select distinct l from Line l left join fetch l.sections")
    List<Line> findAllWithSection();

    @Query(value = "select distinct l from Line l left join fetch l.sections where l.id = :id")
    Optional<Line> findByIdWithSection(@Param("id") Long id);
}
