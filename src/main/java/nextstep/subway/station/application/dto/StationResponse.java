package nextstep.subway.station.application.dto;

import lombok.Getter;

@Getter
public class StationResponse {
    private final long id;
    private final String name;

    public StationResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
