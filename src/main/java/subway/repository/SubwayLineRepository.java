package subway.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.domain.entity.SubwayLine;

public interface SubwayLineRepository extends JpaRepository<SubwayLine, Long> {
}