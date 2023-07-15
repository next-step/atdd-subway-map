package subway.section;

import org.springframework.data.jpa.repository.JpaRepository;
import subway.station.Station;

public interface SubwaySectionRepository extends JpaRepository<SubwaySection, Long> {

    SubwaySection findByUpStation(Station upStation);
    SubwaySection findByDownStation(Station downStation);
}