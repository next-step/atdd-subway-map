package subway.dto.station;

import lombok.Getter;
import subway.domain.station.Station;

@Getter
public class ReadStationResponse {
    private final Long id;
    private final String name;

    public ReadStationResponse(Station station) {
        this.id = station.getId();
        this.name = station.getName().getName();
    }

}
