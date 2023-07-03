package subway.line.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LineRepository extends JpaRepository<Line, Long> {

    @Query("select l from Line l join fetch l.stations s where l.id = :id")
    Optional<Line> findById(Long id);

}
