package nextstep.subway.infra;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SectionRepository extends JpaRepository<Section, Long> {

    List<Section> findByLine(Line line);

    boolean existsByLineAndUpStation(Line line, Station upStation);

    boolean existsByLineAndDownStation(Line line, Station downStation);

    boolean existsByLineAndUpStationOrDownStation(Line line, Station upStation, Station downStation);
}
