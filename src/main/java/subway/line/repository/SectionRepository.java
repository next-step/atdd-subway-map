package subway.line.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.line.entity.Section;

import java.util.List;

public interface SectionRepository extends JpaRepository<Section, Long> {

    List<Section> findByDownStationId(Long downStationId);

}
