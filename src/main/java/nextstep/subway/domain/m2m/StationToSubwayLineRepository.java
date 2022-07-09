package nextstep.subway.domain.m2m;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StationToSubwayLineRepository extends JpaRepository<StationToSubwayLine, Long> {
}
