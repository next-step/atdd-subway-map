package subway.subway.application.out;

import subway.subway.domain.Station;

import java.util.List;

public interface StationListLoadByIdInPort {
    List<Station> findAllIn(List<Station.Id> stationIds);
}
