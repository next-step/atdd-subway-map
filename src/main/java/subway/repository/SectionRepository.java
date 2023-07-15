package subway.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.model.Section;

import java.util.List;

public interface SectionRepository extends JpaRepository<Section, Long> {
    public List<Section> findByLineId(Long lineId);
}
