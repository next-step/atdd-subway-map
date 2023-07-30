package subway.dto;

import java.util.List;
public class LineResponse {

    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stationResponseList;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<StationResponse> getStationResponseList() {
        return stationResponseList;
    }

    public LineResponse(Long id, String name, String color, List<StationResponse> stationResponseList) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stationResponseList = stationResponseList;
    }
}
