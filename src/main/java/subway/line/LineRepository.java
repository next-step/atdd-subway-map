package subway.line;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LineRepository extends JpaRepository<Line, Long> {

    @EntityGraph(attributePaths = {"upStation", "downStation", "sections", "sections.upStation", "sections.downStation"})
    List<Line> findAll();

    @EntityGraph(attributePaths = {"upStation", "downStation", "sections", "sections.upStation", "sections.downStation"})
    Optional<Line> findById(Long lineId);
}