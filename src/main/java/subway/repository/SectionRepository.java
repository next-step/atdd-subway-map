package subway.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.entity.Section;

import java.util.List;

public interface SectionRepository extends JpaRepository<Section, Long> {
    List<Section> findByLineId(final Long lineId);

    Section findByLineIdAndDownStationId(final Long lineId, final Long stationId);
}
