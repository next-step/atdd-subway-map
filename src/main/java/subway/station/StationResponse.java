package subway.station;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StationResponse {
    private Long id;
    private String name;

    public static StationResponse of(Station station) {
        return new StationResponse(station.getId(), station.getName());
    }

    public Station toEntity() {
        return new Station(this.id, this.name);
    }
}
