package subway.station.application.dto.response;

import lombok.Builder;
import lombok.Getter;
import subway.station.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class StationResponse {
    private final Long id;
    private final String name;

    @Builder
    private StationResponse(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public static StationResponse from(final Station station) {
        return StationResponse.builder()
                .id(station.getId())
                .name(station.getName())
                .build();
    }

    public static List<StationResponse> fromList(final List<Station> stations) {
        return stations.stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());
    }
}
