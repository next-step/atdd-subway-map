package nextstep.subway.application.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nextstep.subway.domain.Station;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Getter
public class StationResponse {
    private final Long id;
    private final String name;
    private final LocalDateTime createdDate;
    private final LocalDateTime modifiedDate;

    public static StationResponse of(Station station) {
        return new StationResponse(
                station.getId(),
                station.getName(),
                station.getCreatedDate(),
                station.getModifiedDate()
        );
    }

    public static List<StationResponse> stationResponses(List<Station> stations) {
        return stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }
}
