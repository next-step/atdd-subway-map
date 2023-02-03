package subway.line.infra;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.line.domain.Line;
import subway.line.domain.LineCommandRepository;
import subway.line.domain.LineQueryRepository;

public interface JpaLineRepository
        extends LineQueryRepository, LineCommandRepository, JpaRepository<Line, Long> {
}
