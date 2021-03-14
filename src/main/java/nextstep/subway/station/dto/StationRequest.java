package nextstep.subway.station.dto;

import nextstep.subway.station.domain.Station;

import static nextstep.subway.station.domain.Station.createStation;

public class StationRequest {
    private String name;

    public String getName() {
        return name;
    }

    public Station toStation() {
        return createStation(name);
    }
}
