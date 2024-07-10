package subway.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.domain.SubwayLine;

public interface SubwayLineRepository extends JpaRepository<SubwayLine, Long> {
}
