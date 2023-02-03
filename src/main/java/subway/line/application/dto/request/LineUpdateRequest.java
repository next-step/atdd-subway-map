package subway.line.application.dto.request;

import lombok.Getter;
import subway.line.domain.Line;

@Getter
public class LineUpdateRequest {
    private final String name;
    private final String color;

    public LineUpdateRequest(final String name, final String color) {
        this.name = name;
        this.color = color;
    }

    public Line toEntity() {
        return Line.builder()
                .name(getName())
                .color(getColor())
                .build();
    }
}
