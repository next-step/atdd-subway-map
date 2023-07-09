package subway.model.section;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.model.line.Line;
import subway.model.station.Station;

import java.util.List;
import java.util.Optional;

interface SectionRepository extends JpaRepository<Section, Long> {

    List<Section> findByLine(Line line);

    Optional<Section> findByDownStation(Station downStation);
}