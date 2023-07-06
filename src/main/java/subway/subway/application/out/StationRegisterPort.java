package subway.subway.application.out;

import subway.subway.application.query.StationResponse;
import subway.subway.domain.Station;

public interface StationRegisterPort {
    StationResponse save(Station station);
}
