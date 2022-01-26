package nextstep.subway.domain;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LineRepository extends JpaRepository<Line, Long> {
    boolean existsLineByName(String name);

    Line findLineById(Long lineId);

    @Query(value = "select l from Line l " +
            "join fetch l.sections s " +
            "join fetch s.upStation us " +
            "join fetch s.downStation ds " +
            "where l.id = :lineId")
    Line findLineWithSectionsById(@Param("lineId") Long lineId);

}
