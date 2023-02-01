package subway.line.operator;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.line.domain.Line;
import subway.line.repository.LineCommandRepository;

public interface LineJpaCommandOperator
        extends LineCommandRepository, JpaRepository<Line, Long> {
}