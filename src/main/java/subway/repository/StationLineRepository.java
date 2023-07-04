package subway.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.domain.StationLine;

public interface StationLineRepository extends JpaRepository<StationLine, Long> {
}
