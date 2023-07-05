package subway.controller.dto.response;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import subway.domain.Station;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StationResponse {
    private Long id;
    private String name;

    public StationResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static StationResponse from(Station station) {
        return new StationResponse(station.getId(), station.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
