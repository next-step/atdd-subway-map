package subway.subway.application.out;

import subway.subway.domain.Station;

public interface StationClosePort {
    void closeStation(Station.Id id);
}
