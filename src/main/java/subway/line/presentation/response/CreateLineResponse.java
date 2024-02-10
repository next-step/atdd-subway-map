package subway.line.presentation.response;

import subway.line.domain.Line;
import subway.station.service.StationDto;

import java.util.List;
import java.util.stream.Collectors;

public class CreateLineResponse {

    private Long lineId;

    private String name;

    private String color;

    private List<StationDto> stations;

    private Integer distance;

    private CreateLineResponse() {
    }

    private CreateLineResponse(Long lineId, String name, String color, List<StationDto> stations, Integer distance) {
        this.lineId = lineId;
        this.name = name;
        this.color = color;
        this.stations = stations;
        this.distance = distance;
    }

    public static CreateLineResponse from(Line line) {
        return new CreateLineResponse(
                line.getLineId(),
                line.getName(),
                line.getColor(),
                line.getStations().stream()
                        .distinct()
                        .map(StationDto::from)
                        .collect(Collectors.toList()),
                line.getDistance()
        );
    }

    public Long getLineId() {
        return lineId;
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
