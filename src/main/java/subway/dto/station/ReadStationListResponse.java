package subway.dto.station;

import lombok.Getter;
import subway.domain.station.Station;

@Getter
public class ReadStationListResponse {
    private final Long id;
    private final String name;

    public ReadStationListResponse(Station station) {
        this.id = station.getId();
        this.name = station.getName().getName();
    }

}
