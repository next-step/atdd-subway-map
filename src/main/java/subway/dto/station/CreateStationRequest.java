package subway.dto.station;

import lombok.Getter;
import subway.domain.station.Station;

@Getter
public class CreateStationRequest {
    private String name;

    public Station convertDtoToEntity() {
        return new Station(name);
    }
}
