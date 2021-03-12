package nextstep.subway.line.domain;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LineRepository extends JpaRepository<Line, Long> {

    @Override
    @EntityGraph(attributePaths = {"sections"})
    List<Line> findAll();

    @Override
    @EntityGraph(attributePaths = {"sections"})
    Optional<Line> findById(Long aLong);

    boolean existsByName(String name);
}
