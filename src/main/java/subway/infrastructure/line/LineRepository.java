package subway.infrastructure.line;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.domain.line.entity.Line;

public interface LineRepository  extends JpaRepository<Line, Long> {
}
