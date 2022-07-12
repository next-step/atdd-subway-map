package nextstep.subway.applicaion.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class LineResponse {

    private Long id;

    private String name;

    private String color;

    @JsonProperty("stations")
    private List<StationResponse> stationResponse;

    public LineResponse(Long id, String name, String color,
                        List<StationResponse> stationResponse) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stationResponse = stationResponse;
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

    @JsonProperty("stations")
    public List<StationResponse> getStationResponse() {
        return stationResponse;
    }

}
