package subway.service.dto;

import subway.controller.dto.StationResponse;

import java.util.List;

public class LineDto {
    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations;

    public LineDto(Long id, String name, String color, List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
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
