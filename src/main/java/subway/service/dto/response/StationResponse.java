package subway.service.dto.response;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.domain.Station;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class StationResponse {
    private Long id;
    private String name;

    public StationResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static StationResponse toResponse(final Station station) {
        return new StationResponse(station.getId(), station.getName());
    }

    public static List<StationResponse> toResponses(final Station... stations) {
        return Stream.of(stations)
                .map(StationResponse::toResponse)
                .collect(Collectors.toUnmodifiableList());
    }
}
