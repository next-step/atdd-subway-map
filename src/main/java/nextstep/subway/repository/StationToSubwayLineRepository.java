package nextstep.subway.repository;

import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationToSubwayLine;
import nextstep.subway.domain.SubwayLine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StationToSubwayLineRepository extends JpaRepository<StationToSubwayLine, Long> {

    Optional<StationToSubwayLine> findByStationAndSubwayLine(Station station, SubwayLine subwayLine);
}
