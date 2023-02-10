package subway.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.models.Line;

public interface LineRepository extends JpaRepository<Line, Long> {
}