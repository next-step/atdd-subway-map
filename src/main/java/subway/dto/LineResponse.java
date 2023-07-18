package subway.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import subway.domain.Line;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations;

    public static LineResponse from(Line line) {
        return new LineResponse(line.getId(), line.getName(), line.getColor(), line.getStations().stream().map(StationResponse::from).collect(Collectors.toList()));
    }
}
