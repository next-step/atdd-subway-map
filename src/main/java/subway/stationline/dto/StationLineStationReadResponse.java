package subway.stationline.dto;

public class StationLineStationReadResponse {
    private Long id;
    private String name;

    public StationLineStationReadResponse() {
    }

    public StationLineStationReadResponse(Long id, String name) {
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
