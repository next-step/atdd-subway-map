package subway.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StationResponse {
    private Long id;
    private String name;

    public StationResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static StationResponse createStationResponse(Station station) {
        return new StationResponse(
            station.getId(),
            station.getName()
        );
    }

    public static List<StationResponse> createStationsResponse(Station upStation, Station downStation) {
        return Stream.of(upStation, downStation)
              .map(StationResponse::createStationResponse)
              .collect(Collectors.toList());
    }
}
