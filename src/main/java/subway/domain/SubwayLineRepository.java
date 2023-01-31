package subway.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SubwayLineRepository extends JpaRepository<SubwayLine, Long>, CustomSubwayLineRepository {
}
