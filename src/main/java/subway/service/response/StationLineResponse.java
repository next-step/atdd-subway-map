package subway.service.response;

import java.util.List;
import subway.entity.StationLine;

public class StationLineResponse {

    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations;

    public StationLineResponse() {
    }

    public StationLineResponse(Long id, String name, String color, List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public static StationLineResponse of(final StationLine entity, final List<StationResponse> stations) {
        return new StationLineResponse(
            entity.getId(),
            entity.getName(),
            entity.getColor(),
            stations
        );
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public String getColor() {
        return color;
    }
}
