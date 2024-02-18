package subway.domians.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.domians.domain.Line;

public interface LineRepository extends JpaRepository<Line, Long> {

}
