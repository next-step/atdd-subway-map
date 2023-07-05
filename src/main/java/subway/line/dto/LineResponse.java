package subway.line.dto;

import lombok.Builder;
import lombok.Getter;
import subway.line.model.Line;
import subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class LineResponse {
    private Long id;

    private String name;

    private String color;

    public static LineResponse from(Line line) {
        return LineResponse.builder()
                .id(line.getId())
                .name(line.getName())
                .color(line.getColor())
                .build();
    }
}
