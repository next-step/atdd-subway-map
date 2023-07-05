package subway.line.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.line.model.Line;
import subway.line.model.LineStation;

public interface LineStationRepository extends JpaRepository<LineStation, Long> {

    void deleteByLine(Line line);

}
