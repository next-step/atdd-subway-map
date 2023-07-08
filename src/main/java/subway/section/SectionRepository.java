package subway.section;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SectionRepository extends JpaRepository<Section, Long> {
    void deleteAllByLineId(Long lineId);

    Section findSectionByLineIdAndUpStationIdAndDownStationId(Long upStationId, Long downStationId, Long lineId);
}
