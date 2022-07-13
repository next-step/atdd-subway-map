package nextstep.subway.domain.section;

import java.util.Optional;
import nextstep.subway.domain.station.Station;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SectionRepository extends JpaRepository<Section, Long> {
    void deleteAllByLineId(Long lineId);
    Optional<Section> findByLineIdAndStation(Long lineId, Station station);
}
