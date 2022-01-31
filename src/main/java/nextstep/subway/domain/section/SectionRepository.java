package nextstep.subway.domain.section;

import nextstep.subway.domain.station.Station;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SectionRepository extends JpaRepository<Section, Long> {
    boolean existsByUpStationAndDownStation(Station upStation, Station downStation);
}
