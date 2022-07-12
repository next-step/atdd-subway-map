package nextstep.subway.applicaion.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class LineResponse {

    private Long id;

    private String name;

    private String color;

    @JsonProperty("stations")
    private List<StationResponse> stationResponse;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<StationResponse> getStationResponse() {
        return stationResponse;
    }

}
