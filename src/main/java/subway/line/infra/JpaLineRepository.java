package subway.line.infra;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.line.domain.Line;
import subway.line.domain.LineRepository;

public interface JpaLineRepository extends LineRepository, JpaRepository<Line, Long> {
}
