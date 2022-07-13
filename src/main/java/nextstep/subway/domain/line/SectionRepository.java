package nextstep.subway.domain.line;

import nextstep.subway.domain.station.Station;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SectionRepository extends JpaRepository<Section, Long> {

    List<Section> findByLine(Line line);

    boolean existsByLineAndUpStation(Line line, Station upStation);

    boolean existsByLineAndDownStation(Line line, Station downStation);

    boolean existsByLineAndUpStationOrDownStation(Line line, Station upStation, Station downStation);
}
