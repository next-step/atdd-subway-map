package subway.station.dto;

import lombok.Builder;
import lombok.Getter;
import subway.station.domain.Station;

@Getter
public class StationResponse {
    private Long id;
    private String name;

    @Builder
    private StationResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static StationResponse fromDomain(Station station) {
        return StationResponse.builder()
                .id(station.getId())
                .name(station.getName())
                .build();
    }

}