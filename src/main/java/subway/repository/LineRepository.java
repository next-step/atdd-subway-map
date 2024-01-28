package subway.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.service.line.Line;

public interface LineRepository  extends JpaRepository<Line, Long> {
}
