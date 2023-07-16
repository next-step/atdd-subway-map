package subway.subway.application.out;

import subway.subway.domain.Station;

import java.util.List;

public interface StationLoadPort {
    Station findOne(Station.Id stationId);

}
