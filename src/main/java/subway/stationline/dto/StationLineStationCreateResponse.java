package subway.stationline.dto;

public class StationLineStationCreateResponse {
    private Long id;
    private String name;

    public StationLineStationCreateResponse() {
    }

    public StationLineStationCreateResponse(Long id, String name) {
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
