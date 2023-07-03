package subway.subwayline;

import subway.station.StationResponse;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;


public class SubwayLineResponse {
    private final Long id;
    private final String name;
    private final String color;
    private final Integer distance;
    private final Set<StationResponse> stations;

    public SubwayLineResponse(Long id, String name, String color, Integer distance, Set<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.distance = distance;
        this.stations = stations;
    }

    public static SubwayLineResponse from(SubwayLineDto dto) {
        return new SubwayLineResponse(
                dto.getId(),
                dto.getName(),
                dto.getColor(),
                dto.getDistance(),
                dto.getStationDto().stream()
                        .map(StationResponse::from)
                        .sorted(Comparator.comparing(StationResponse::getId))
                        .collect(Collectors.toCollection(LinkedHashSet::new))
        );
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Integer getDistance() {
        return distance;
    }

    public Set<StationResponse> getStations() {
        return stations;
    }
}
