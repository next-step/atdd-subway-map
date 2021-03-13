package nextstep.subway.station.dto;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationStatus;

public class StationRequest {
    private String name;

    public String getName() {
        return name;
    }

    public Station toStation() {
        return new Station(name);
    }
}
