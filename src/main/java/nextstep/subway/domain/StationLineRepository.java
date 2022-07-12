package nextstep.subway.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StationLineRepository extends JpaRepository<Line, Long> {
}
