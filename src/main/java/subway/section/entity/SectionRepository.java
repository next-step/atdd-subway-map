package subway.section.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.line.entity.Line;
import subway.station.entity.Station;

import java.util.List;

public interface SectionRepository extends JpaRepository<Section, Long> {
    List<Section> findAllByLine(Line line);

    void deleteByDownStation(Station station);
}
