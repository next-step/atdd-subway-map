package subway.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.domain.Line;

public interface LineRepository extends JpaRepository<Line, Long> {

}
