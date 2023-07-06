package subway.section.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import subway.line.domain.Line;
import subway.section.domain.Section;
import subway.station.domain.Station;

@Repository
public interface SectionRepository extends JpaRepository<Section, Long> {
    public Optional<Section> findByLineAndDownStation(Line line, Station station);
}
