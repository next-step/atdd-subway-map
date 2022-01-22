package nextstep.subway.domain;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LineRepository extends JpaRepository<Line, Long> {

	Optional<Line> findByName(String name);

	boolean existsByNameAndIdIsNot(String name, long id);

	boolean existsByName(String name);
}
