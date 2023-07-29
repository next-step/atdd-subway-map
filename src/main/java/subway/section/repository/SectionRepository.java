package subway.section.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.section.domain.Section;

import java.util.List;
import java.util.Optional;

public interface SectionRepository extends JpaRepository<Section, Long> {
    List<Section> findAllByLine_Id(Long lineId);
    
    Optional<Section> findByLine_IdAndDownStation_Id(Long lineId, Long downStationId);
}
