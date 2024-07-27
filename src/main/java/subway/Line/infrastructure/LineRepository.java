package subway.Line.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.Line.domain.Line;

public interface LineRepository extends JpaRepository<Line, Long> {

}
