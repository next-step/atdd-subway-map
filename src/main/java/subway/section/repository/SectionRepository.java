package subway.section.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.section.domain.Section;

import java.util.List;

public interface SectionRepository extends JpaRepository<Section, Long> {
    List<Section> findAllByLine_Id(Long lineId);
}
