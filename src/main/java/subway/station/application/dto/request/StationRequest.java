package subway.station.application.dto.request;

import lombok.Getter;
import subway.station.domain.Station;

@Getter
public class StationRequest {
    private String name;

    public Station toEntity() {
        return Station.builder()
                .name(getName())
                .build();
    }
}
