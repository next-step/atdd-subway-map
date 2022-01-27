package nextstep.subway.applicaion.line.repository;

import nextstep.subway.applicaion.line.domain.Line;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LineRepository extends JpaRepository<Line, Long> {

	boolean existsByNameAndIdIsNot(String name, long id);

	boolean existsByName(String name);
}
