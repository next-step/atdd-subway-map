package nextstep.subway.repository;

import nextstep.subway.domain.Station;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Line;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SectionRepository extends JpaRepository<Section, Long> {

    Optional<Section> findByUpStationAndDownStationAndLine(Station upStation, Station downStation, Line line);
}
