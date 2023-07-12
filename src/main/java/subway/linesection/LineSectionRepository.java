package subway.linesection;

import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface LineSectionRepository extends JpaRepository<LineSection, LineSectionPK> {
    List<LineSection> findByLineId(Long lineId);

    Optional<LineSection> findByLineIdAndNextStationIdIsNull(Long lineId);
}
