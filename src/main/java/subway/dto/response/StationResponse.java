package subway.dto.response;

import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;
import subway.models.Station;

@Getter
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

    public static List<StationResponse> of(List<Station> stations) {
        return stations.stream().map(StationResponse::of).collect(Collectors.toList());
    }
}
