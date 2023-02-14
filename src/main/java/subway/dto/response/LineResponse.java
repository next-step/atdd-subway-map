package subway.dto.response;

import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;
import subway.models.Line;

@Getter
@Builder
public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations;

    public static LineResponse of(Line line) {
        return LineResponse.builder()
            .id(line.getId())
            .name(line.getName())
            .color(line.getColor())
            .stations(line.getSections().getStations().stream()
                .map(StationResponse::of).collect(Collectors.toList()))
            .build();
    }

    public static List<LineResponse> of(List<Line> lines) {
        return lines.stream().map(LineResponse::of).collect(Collectors.toList());
    }
}
