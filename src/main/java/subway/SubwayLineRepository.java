package subway;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubwayLineRepository extends JpaRepository<SubwayLine, Long> {

    Optional<SubwayLine> findById(Long id);
}
