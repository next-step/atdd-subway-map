package subway.station.application.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Builder;
import lombok.Getter;
import subway.station.domain.Station;

@Getter
public class StationRequest {
    private final String name;

    @JsonCreator
    @Builder
    private StationRequest(final String name) {
        this.name = name;
    }

    public Station toEntity() {
        return Station.builder()
                .name(getName())
                .build();
    }
}
