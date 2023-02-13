package subway.line.section;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LineSectionRepository extends JpaRepository<LineSection, Long> {

    List<LineSection> findAllByLineId(Long lineId);
}
