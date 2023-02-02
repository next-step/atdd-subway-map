package subway.line.application.dto.response;

import lombok.Builder;
import lombok.Getter;
import subway.line.domain.Line;
import subway.station.application.dto.response.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class LineResponse {
    private Long id;
    private String name;
    private String color;

    private List<StationResponse> stations;

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
                .stations(List.of(
                        StationResponse.builder()
                                .id(line.getUpStation().getId())
                                .name(line.getUpStation().getName())
                                .build(),
                        StationResponse.builder()
                                .id(line.getDownStation().getId())
                                .name(line.getDownStation().getName())
                                .build()
                ))
                .build();
    }

    public static List<LineResponse> fromList(final List<Line> lines) {
        return lines.stream()
                .map(LineResponse::from)
                .collect(Collectors.toList());
    }
}
