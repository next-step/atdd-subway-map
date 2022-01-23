package nextstep.subway.line.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import nextstep.subway.line.domain.model.Line;

public interface LineRepository extends JpaRepository<Line, Long> {
    boolean existsByName(String name);

    @Query("SELECT l FROM Line l"
        + " JOIN FETCH l.sections"
        + " WHERE l.id = :id")
    Optional<Line> findByIdWithSections(Long id);
}
