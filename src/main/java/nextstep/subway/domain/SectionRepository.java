package nextstep.subway.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SectionRepository extends JpaRepository<Section, Long> {
    List<Section> findSectionsByLineId(long lineId);
    void deleteSectionByDownStationIdAndLineId(long downStationId, long lineId);
    Section findSectionByDownStationIdAndLineId(long downStationId, long lineId);
}
