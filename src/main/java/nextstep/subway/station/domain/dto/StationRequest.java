package nextstep.subway.station.domain.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

public class StationRequest {
    private String name;

    @JsonCreator
    public StationRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
