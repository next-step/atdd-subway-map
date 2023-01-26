package subway.line.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import subway.line.dto.LineResponse;
import subway.line.entity.Line;

import java.util.List;

public interface LineRepository extends JpaRepository<Line, Long> {

    @Query("select l " +
            "from Line l " +
            "join fetch l.downStation " +
            "join fetch l.upStation")
    List<Line> findAllLine();
}
