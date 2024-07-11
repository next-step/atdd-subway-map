package subway.section;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SectionRepository extends JpaRepository<Section, Long> {

    Optional<Section> findByLineIdAndDownStationId(Long lineId, Long downStationId);

    List<Section> findAllByLineIdOrderById(Long lineId);
}
