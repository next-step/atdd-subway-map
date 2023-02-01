package subway.stationline.dto;

public class StationLineStationReadListResponse {
    private Long id;
    private String name;

    public StationLineStationReadListResponse() {
    }

    public StationLineStationReadListResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
