package nextstep.subway.domain;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LineRepository extends JpaRepository<Line, Long> {
    boolean existsLineByName(String name);

    Line findLineById(Long lineId);

    @Query(value = "select l from Line l " +
            "left join l.sections s " +
            "left join s.upStation us " +
            "left join s.downStation ds " +
            "where l.id = :lineId")
    Line findLineWithSectionsById(@Param("lineId") Long lineId);

}
