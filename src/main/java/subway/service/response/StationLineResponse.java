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

    public static StationLineResponse of(final StationLine entity) {
        return new StationLineResponse(
            entity.getId(),
            entity.getName(),
            entity.getColor(),
            List.of(
                new StationResponse(entity.getUpStationId(), "상행 종점"),
                new StationResponse(entity.getDownStationId(), "하행 종점")
            )
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
