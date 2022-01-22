package nextstep.subway.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface LineRepository extends JpaRepository<Line, Long> {

	Line findByName(@Param("name") String name);
}
