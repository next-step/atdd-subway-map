package subway.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import subway.domain.Line;
import subway.domain.Section;

import java.util.List;

public interface SectionRepository extends JpaRepository<Section, Long> {
    List<Section> findAllByLineIn(List<Line> lines);
    List<Section> findByLine(Line line);
    @Modifying
    void deleteByLine(Line line);
}
