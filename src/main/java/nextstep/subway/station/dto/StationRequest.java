package nextstep.subway.station.dto;

import nextstep.subway.station.domain.Station;

public class StationRequest {
    private Long id;

    private String name;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Station toStation() {
        return new Station(name);
    }
}
