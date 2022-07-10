package nextstep.subway.infra;

import nextstep.subway.domain.line.Line;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaLineRepository extends JpaRepository<Line, Long> {
}
