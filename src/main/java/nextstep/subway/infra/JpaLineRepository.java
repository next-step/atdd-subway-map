package nextstep.subway.infra;

import nextstep.subway.domain.Line;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaLineRepository extends JpaRepository<Line, Long> {
}
