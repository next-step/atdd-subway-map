package subway.section.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.line.jpa.Line;
import subway.station.jpa.Station;

import java.util.List;

public interface SectionRepository extends JpaRepository<Section, Long> {
    List<Section> findAllByLine(Line line);

    void deleteByDownStation(Station station);
}
