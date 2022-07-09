package nextstep.subway.applicaion.dto;

import lombok.Builder;
import lombok.Getter;
import nextstep.subway.domain.Station;

@Getter
@Builder
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
