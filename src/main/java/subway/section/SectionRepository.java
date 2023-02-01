package subway.section;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.line.Line;

import java.util.List;

public interface SectionRepository extends JpaRepository<Section, Long> {
    void deleteByLineAndDownStationId(Line line, Long downStationId);

    void deleteByLine(Line line);

    List<Section> findAllByLine(Line line);
}
