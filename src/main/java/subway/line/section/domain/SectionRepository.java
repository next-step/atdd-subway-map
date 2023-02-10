package subway.line.section.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import subway.line.domain.Line;
import subway.station.domain.Station;

@Repository
public interface SectionRepository extends JpaRepository<Section, Long> {
    Section findByLineAndDownStation(Line line, Station downStation);
}
