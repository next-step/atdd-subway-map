package subway.stationline;

import java.util.List;
import subway.station.StationResponse;

public class StationLineResponse {

    private Long id;
    private String name;
    private List<StationResponse> stations;
    private int distance;

    public StationLineResponse() {
    }

    public StationLineResponse(Long id, String name, List<StationResponse> stations, int distance) {
        this.id = id;
        this.name = name;
        this.stations = stations;
        this.distance = distance;
    }

    public static StationLineResponse of(final StationLine entity) {
        return new StationLineResponse(
            entity.getId(),
            entity.getName(),
            List.of(
                new StationResponse(entity.getUpStationId(), "상행 종점"),
                new StationResponse(entity.getDownStationId(), "하행 종점")
            ),
            entity.getDistance()
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

    public int getDistance() {
        return distance;
    }
}
