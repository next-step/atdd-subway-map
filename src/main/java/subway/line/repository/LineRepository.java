package subway.line.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import subway.line.repository.domain.Line;

import java.util.List;

public interface LineRepository extends JpaRepository<Line, Long> {
    @Query("select line from Line line join fetch line.upStation join fetch line.downStation")
    List<Line> findAllWithLines();

}
