package subway.line.operator;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.line.domain.Line;
import subway.line.repository.LineQueryRepository;


public interface LineJpaQueryOperator
        extends LineQueryRepository, JpaRepository<Line, Long> {
}