package subway.domain.station.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.domain.station.entity.Line;

public interface LineRepository extends JpaRepository<Line, Long> {
}
