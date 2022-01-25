package nextstep.subway.domain;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LineRepository extends JpaRepository<Line, Long> {
    boolean existsLineByName(String name);

    Line findLineById(Long lineId);

    @EntityGraph(attributePaths = "sections")
    Line findLineWithSectionsById(Long lineId);

}
