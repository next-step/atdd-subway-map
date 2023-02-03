package subway.station;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StationResponse {
    private Long id;

    private Long lineId;
    private String name;

    public static StationResponse entityToResponse(Station station) {
        return new StationResponse(station.getId(), station.getLineId(), station.getName());
    }
}
