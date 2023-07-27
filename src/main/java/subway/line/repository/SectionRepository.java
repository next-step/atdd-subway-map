package subway.line.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import subway.line.domain.Line;
import subway.line.domain.Section;
import subway.station.domain.Station;

import java.util.List;
import java.util.Optional;

@Repository
public interface SectionRepository extends JpaRepository<Section, Long> {

    Optional<Section> findByLineAndStation(Line line, Station station);

    List<Section> findAllByLine(Line line);

    Optional<Section> findTopByLineOrderByIdDesc(Line line);

}
