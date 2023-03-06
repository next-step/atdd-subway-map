package subway.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.domain.Section;

import java.util.List;

public interface SectionRepository extends JpaRepository<Section, Long> {

    List<Section> findAllByLineId(Long lindId);

    Section findByLineIdAndDownStationId(Long lineId, Long stationId);


}
