package nextstep.subway.repository;

import nextstep.subway.domain.Distance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DistanceRepository extends JpaRepository<Distance, Long> {
}