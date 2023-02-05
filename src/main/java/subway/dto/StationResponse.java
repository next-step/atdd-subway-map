package subway.dto;

import java.util.List;
import java.util.stream.Collectors;
import subway.domain.Station;

public class StationResponse {

    private final Long id;
    private final String name;

    public StationResponse(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public static List<StationResponse> by(final List<Station> stations) {
        return stations.stream()
                .map(station -> new StationResponse(station.getId(), station.getName()))
                .collect(Collectors.toUnmodifiableList());
    }

    public static StationResponse by(final Station station) {
        return new StationResponse(station.getId(), station.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
