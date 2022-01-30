package nextstep.subway.application.repository;

import nextstep.subway.domain.Line;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LineRepository extends JpaRepository<Line, Long> {

	boolean existsByNameAndIdIsNot(String name, long id);

	boolean existsByName(String name);
}
