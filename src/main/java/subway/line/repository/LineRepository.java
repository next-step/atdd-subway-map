package subway.line.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.line.repository.entity.Line;

public interface LineRepository extends JpaRepository<Line, Long> {
}