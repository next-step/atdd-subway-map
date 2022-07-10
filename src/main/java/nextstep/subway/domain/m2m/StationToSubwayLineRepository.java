package nextstep.subway.domain.m2m;

import nextstep.subway.domain.station.Station;
import nextstep.subway.domain.subwayLine.SubwayLine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StationToSubwayLineRepository extends JpaRepository<StationToSubwayLine, Long> {

    Optional<StationToSubwayLine> findByStationAndSubwayLine(Station station, SubwayLine subwayLine);
}
