package subway.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;

public class StationResponse {
    private final Long id;
    private final String name;

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

    public static List<StationResponse> createStationsResponse(Sections sections) {
        return sections.getSections().stream()
                       .flatMap(section ->
                                    Stream.of(section.getUpStation(), section.getDownStation()))
                       .distinct()
                       .map(StationResponse::createStationResponse)
                       .collect(Collectors.toList());
    }
}
