package subway.line.presentation.response;

import subway.line.domain.Line;
import subway.station.service.StationDto;

import java.util.List;
import java.util.stream.Collectors;

public class AddSectionResponse {

    private Long id;

    private String name;

    private String color;

    private List<StationDto> stations;

    private Integer distance;

    private AddSectionResponse() {
    }

    private AddSectionResponse(Long id, String name, String color, List<StationDto> stations, Integer distance) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
        this.distance = distance;
    }

    public static AddSectionResponse from(Line line) {
        return new AddSectionResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                line.getStations().stream()
                        .distinct()
                        .map(StationDto::from)
                        .collect(Collectors.toList()),
                line.getDistance()
        );
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

    public List<StationDto> getStations() {
        return stations;
    }

    public Integer getDistance() {
        return distance;
    }

}
