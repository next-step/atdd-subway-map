package subway.section;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SectionRepository extends JpaRepository<Section, Long> {

    int countByLineId(Long lineId);

    Section findByLineIdAndDownStationId(Long lineId, Long downStationId);
}
