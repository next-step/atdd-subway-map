package subway.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.domain.entity.Line;

public interface LineRepository extends JpaRepository<Line, Long> {

}
