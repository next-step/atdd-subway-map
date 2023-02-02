package subway.line;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LineRepository extends JpaRepository<Line, Long> {

    @Query(value = "select distinct l from Line l left join fetch l.upStation left join fetch l.downStation left join fetch l.lastLineSection")
    List<Line> findAllWithDefault();

    @Query(value = "select distinct l from Line l left join fetch l.upStation left join fetch l.downStation left join fetch l.lastLineSection left join fetch l.lineSections ls left join fetch ls.section s where l.id = :id")
    Optional<Line> findByIdWithLineSections(@Param("id") Long id);

    @Query(value = "select distinct l from Line l left join fetch l.upStation left join fetch l.downStation left join fetch l.lastLineSection left join fetch l.lineStations ls left join fetch ls.station where l.id = :id")
    Optional<Line> findByIdWithLineStations(@Param("id") Long id);
}
