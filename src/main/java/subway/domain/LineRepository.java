package subway.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LineRepository extends JpaRepository<Line, Long> {

    @Override
    @Query(value = "SELECT l FROM Line l join fetch l.upStation join fetch l.downStation")
    List<Line> findAll();
}
