package subway.line;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LineRepository extends JpaRepository<Line, Long>, LineCustomRepository {
    @Query(value = "select distinct l from Line l left join fetch l.stations")
    List<Line> findAllWithStation();
}
