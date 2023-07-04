package subway.station.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.line.model.LineStation;
import subway.station.model.Station;

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

    public static StationResponse from(LineStation station) {
        return StationResponse.builder()
                .id(station.getStation().getId())
                .name(station.getStation().getName())
                .build();
    }
}
