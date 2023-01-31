package subway.line.application.dto.response;

import lombok.Builder;
import lombok.Getter;
import subway.line.domain.Line;
import subway.station.domain.Station;

import java.util.List;

@Getter
public class LineCreateResponse {
    private Long id;
    private String name;
    private String color;
    private List<Station> stations;

    @Builder
    private LineCreateResponse(final Long id, final String name,
                               final String color, final List<Station> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
    }

    public static LineCreateResponse from(final Line line) {
        return LineCreateResponse.builder()
                .id(line.getId())
                .name(line.getName())
                .color(line.getColor())
                .stations(List.of(
                        line.getUpStation(),
                        line.getDownStation()))
                .build();
    }
}
