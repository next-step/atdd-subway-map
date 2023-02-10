package subway.line.section;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import subway.line.Line;
import subway.station.Station;

@Repository
public interface SectionRepository extends JpaRepository<Section, Long> {
    Section findByLineAndDownStation(Line line, Station downStation);
}
