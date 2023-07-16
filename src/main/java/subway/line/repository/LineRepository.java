package subway.line.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import subway.line.domain.Line;

import java.util.Optional;

@Repository
public interface LineRepository extends JpaRepository<Line, Long> {

    Optional<Line> findByName(String name);
}
