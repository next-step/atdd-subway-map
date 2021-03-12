package nextstep.subway.station.domain;

import nextstep.subway.station.dto.StationResponse;
import nextstep.subway.station.exception.StationAlreadyExistsException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Stations {
    private final List<Station> stations;

    private Stations(List<Station> stations) {
        this.stations = Collections.unmodifiableList(stations);
    }

    public static Stations of(List<Station> stations) {
        return new Stations(stations);
    }

    public void validateExistStation() {
        if (stations.size() > 0) {
            throw new StationAlreadyExistsException();
        }
    }

    public List<StationResponse> toResponses() {
        return stations.stream()
                .map(station -> StationResponse.of(station))
                .collect(Collectors.toList());
    }
}
