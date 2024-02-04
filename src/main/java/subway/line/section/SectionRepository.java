package subway.line.section;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.line.Line;

import java.util.List;

public interface SectionRepository extends JpaRepository<Section, SectionId> {
    List<Section> findAllByLineId(Long id);
}
