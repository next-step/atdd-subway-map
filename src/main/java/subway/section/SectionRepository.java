package subway.section;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SectionRepository extends JpaRepository<Section, Long> {
    Section findByDownStationIdAndLineId(Long downStationId, Long lineId);
}
