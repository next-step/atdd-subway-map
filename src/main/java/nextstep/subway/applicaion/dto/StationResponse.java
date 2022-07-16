package nextstep.subway.applicaion.dto;

import lombok.Getter;

@Getter
public class StationResponse {
    private Long id;
    private String name;

    public StationResponse() {}

    public StationResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static StationResponse createStationResponse(Long id, String name) {
        return new StationResponse(id, name);
    }
}
