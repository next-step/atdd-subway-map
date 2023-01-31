package subway.presentation.station.dto.response;

import lombok.Builder;
import lombok.Data;
import subway.domain.station.Station;

@Data
public class StationResponse {
    private Long id;
    private String name;

    @Builder
    public StationResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static StationResponse of(Station station) {
        return StationResponse.builder()
                .id(station.getId())
                .name(station.getName())
                .build();
    }
}