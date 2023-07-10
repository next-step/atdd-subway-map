package subway.controller.dto.station;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.model.station.Station;
import subway.model.station.StationDTO;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StationResponse {
    private Long id;
    private String name;

    public static StationResponse from(Station station) {
        return StationResponse.builder()
                              .id(station.getId())
                              .name(station.getName())
                              .build();
    }

    public static StationResponse from(StationDTO station) {
        return StationResponse.builder()
                              .id(station.getId())
                              .name(station.getName())
                              .build();
    }
}
