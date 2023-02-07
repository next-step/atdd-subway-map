package subway.controller.station.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import subway.domain.Station;

@Getter
@AllArgsConstructor
public class StationResponse {
    private Long id;

    private String name;

    public static StationResponse entityToResponse(Station station) {
        return new StationResponse(station.getId(), station.getName());
    }
}
