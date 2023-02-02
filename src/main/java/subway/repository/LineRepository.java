package subway.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.model.Line;

public interface LineRepository extends JpaRepository<Line, Long> {
}
