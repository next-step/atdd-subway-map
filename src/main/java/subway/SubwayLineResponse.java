package subway;

import java.util.List;
import java.util.stream.Collectors;

public final class SubwayLineResponse {
    private final Long id;
    private final String name;
    private final String color;
    private final List<StationResponse> stations;

    private SubwayLineResponse(Long id, String name, String color, List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public static SubwayLineResponse from(SubwayLine subwayLine) {
        List<StationResponse> stationResponses = subwayLine.getStations().stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());

        return new SubwayLineResponse(
                subwayLine.getId(),
                subwayLine.getName(),
                subwayLine.getColor(),
                stationResponses
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

    public List<StationResponse> getStations() {
        return stations;
    }
}
