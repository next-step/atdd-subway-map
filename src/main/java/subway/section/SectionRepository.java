package subway.section;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import subway.line.Line;

public interface SectionRepository extends JpaRepository<Section, Long> {

    List<Section> findAllByLine(Line line);
}
