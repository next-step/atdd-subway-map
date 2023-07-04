package subway.subwayline.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.subwayline.entity.SubwayLine;

public interface SubwayLineRepository extends JpaRepository<SubwayLine, Long> {
}
