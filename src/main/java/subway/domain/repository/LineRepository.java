package subway.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.domain.entity.line.Line;

public interface LineRepository extends JpaRepository<Line, Long> {
}
