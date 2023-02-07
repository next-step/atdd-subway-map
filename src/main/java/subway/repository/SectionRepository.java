package subway.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.domain.Section;

import java.util.List;

public interface SectionRepository extends JpaRepository<Section, Long> {
    List<Section> findByLineId(Long line_id);
}
