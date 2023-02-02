package subway.line.application.dto.response;

import lombok.Builder;
import lombok.Getter;
import subway.line.domain.Line;
import subway.station.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class LineResponse {
    private Long id;
    private String name;
    private String color;

    private List<Station> stations;

    @Builder
    private LineResponse(final Long id, final String name,
                         final String color, final List<Station> stations) {
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
                .stations(List.of(
                        line.getUpStation(),
                        line.getDownStation()))
                .build();
    }

    public static List<LineResponse> fromList(final List<Line> lines) {
        return lines.stream()
                .map(LineResponse::from)
                .collect(Collectors.toList());
    }
}
