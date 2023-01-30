package subway.dto;

import subway.domain.Station;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StationResponse {
    private Long id;
    private String name;

    public StationResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static List<StationResponse> asList(Station... stations) {

        return Arrays.asList(stations)
                .stream()
                .map(StationResponse::of)
                .collect(Collectors.toUnmodifiableList());
    }

    public static StationResponse of(Station station) {
        return new StationResponse(station.getId(), station.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
