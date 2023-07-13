package subway.line.section;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SectionRepository extends JpaRepository<Section, Long> {

    List<Section> findAllByLineId(Long lineId);
}
