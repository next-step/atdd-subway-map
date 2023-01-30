package subway.controller.response;

import lombok.Builder;
import lombok.Getter;
import subway.repository.entity.Station;

@Builder
@Getter
public class StationResponse {

    private Long id;

    private String name;

    public static StationResponse from(Station station) {
        return StationResponse.builder()
                .id(station.getId())
                .name(station.getName())
                .build();
    }
}
