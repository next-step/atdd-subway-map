package subway.line.application.dto.response;

import lombok.Builder;
import lombok.Getter;
import subway.line.domain.Line;
import subway.station.application.dto.response.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class LineResponse {

    private final Long id;
    private final String name;
    private final String color;
    private final List<StationResponse> stations;

    @Builder
    private LineResponse(final Long id, final String name,
                         final String color, final List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public static LineResponse from(final Line line) {
        return LineResponse.builder()
                .id(line.getId())
                .name(line.getName())
                .color(line.getColor())
                .stations(StationResponse.fromList(line.getSections().getStations()))
                .build();
    }

    public static List<LineResponse> fromList(final List<Line> lines) {
        return lines.stream()
                .map(LineResponse::from)
                .collect(Collectors.toList());
    }
}
