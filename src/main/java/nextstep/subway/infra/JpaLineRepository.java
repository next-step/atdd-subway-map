package nextstep.subway.infra;

import nextstep.subway.domain.line.Line;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaLineRepository extends JpaRepository<Line, Long> {
}
