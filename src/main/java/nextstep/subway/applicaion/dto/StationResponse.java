package nextstep.subway.applicaion.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nextstep.subway.domain.station.Station;

@Getter
@RequiredArgsConstructor
public class StationResponse {
    private final Long id;
    private final String name;

    public static StationResponse fromStation(Station station) {
        return new StationResponse(station.getId(), station.getName());
    }
}
