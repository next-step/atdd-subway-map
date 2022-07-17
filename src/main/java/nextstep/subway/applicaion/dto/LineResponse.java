package nextstep.subway.applicaion.dto;

import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import nextstep.subway.domain.line.Line;

@Getter
public class LineResponse {
    private final Long id;
    private final String name;
    private final String color;
    private final List<StationResponse> stations;

    public LineResponse(Long id, String name, String color, List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public static LineResponse fromLine(Line line) {
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                line.getStations()
                        .stream()
                        .map(StationResponse::fromStation)
                        .collect(Collectors.toList())
        );
    }
}
