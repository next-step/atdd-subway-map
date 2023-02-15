package subway.station.domain.line;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LineRepository extends JpaRepository<Line, Long> {

    @Query("select l from Line l " +
            "left join fetch l.sections.sections s " +
            "left join fetch s.upStation " +
            "left join fetch s.downStation " +
            "where l.id=:id")
    Line findLineWithSectionsAndStationsById(@Param("id") Long id);

    @Query("select l from Line l " +
            "left join fetch l.sections.sections s " +
            "left join fetch s.upStation " +
            "left join fetch s.downStation")
    List<Line> findAllLinesWithSectionsAndStations();
}
