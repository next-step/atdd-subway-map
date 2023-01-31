package subway.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.entity.Line;

public interface LineRepository extends JpaRepository<Line, Long> {
}
