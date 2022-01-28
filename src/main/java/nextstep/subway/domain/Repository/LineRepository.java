package nextstep.subway.domain.Repository;

import nextstep.subway.domain.Entity.Line;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface LineRepository extends JpaRepository<Line, Long> {

	Line findByName(@Param("name") String name);
}
