package nextstep.subway.applicaion.line.repository;

import nextstep.subway.applicaion.line.domain.Line;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LineRepository extends JpaRepository<Line, Long> {

	Optional<Line> findByName(String name);

	boolean existsByNameAndIdIsNot(String name, long id);

	boolean existsByName(String name);
}
