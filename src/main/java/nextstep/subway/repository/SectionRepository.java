package nextstep.subway.repository;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SectionRepository extends JpaRepository<Section, Long> {

    Optional<Section> findByUpStationAndDownStationAndLine(Station upStation, Station downStation, Line line);

    Optional<Section> findByLineAndDownStation(Line line, Station downStation);

    Optional<Section> findByLineAndUpStation(Line line, Station upStation);

    List<Section> findAllByLine(Line line);
}
