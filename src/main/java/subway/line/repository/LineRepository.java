package subway.line.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import subway.line.domain.Line;

import java.util.Optional;

public interface LineRepository extends JpaRepository<Line, Long> {

    @Query("select l from Line l join fetch l.upStation join fetch l.downStation where l.id = :id")
    public Optional<Line> findByIdFetchEager(Long id);
}
