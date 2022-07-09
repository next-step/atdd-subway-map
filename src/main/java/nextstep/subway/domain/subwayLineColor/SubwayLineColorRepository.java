package nextstep.subway.domain.subwayLineColor;

import nextstep.subway.domain.subwayLine.SubwayLineRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubwayLineColorRepository extends JpaRepository<SubwayLineRepository, Long> {
}
