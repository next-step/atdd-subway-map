package subway.station.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.station.entity.Station;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StationResponse {

    private Long id;
    private String name;

    public static StationResponse from(Station station) {
        return new StationResponse(station.getId(), station.getName());
    }
}
